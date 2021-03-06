package net.pumbas.deadlinebot.authorization;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.CalendarScopes;

import net.pumbas.deadlinebot.App;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AuthorizationService
{
    public static final String AUTHORIZE_REDIRECT_URL = "http://localhost:8080" + App.API_V1 + "/authorize/token";

    public static final String TOKENS_FILE_PATH = "tokens";
    public static final String CREDENTIALS_FILE_PATH = "credentials.json";
    public static final List<String> CALENDAR_SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);

    private Map<String, AuthorizationState> sessionAuthorizationStates = new ConcurrentHashMap<>();

    private AuthorizationCodeFlow googleFlow;

    @Bean
    public CommandLineRunner initialiseService() {
        return (args) -> {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            InputStream in = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

            this.googleFlow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, clientSecrets, CALENDAR_SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_FILE_PATH)))
                .setAccessType("offline")
                .build();
        };
    }

    public void updateAuthorizationState(String sessionId, AuthorizationState newState) {
        if (newState == AuthorizationState.UNAUTHORIZED)
            this.sessionAuthorizationStates.remove(sessionId);
        else
            this.sessionAuthorizationStates.put(sessionId, newState);
    }

    public AuthorizationState getAuthorizationState(String sessionId) {
        return this.sessionAuthorizationStates.getOrDefault(sessionId, AuthorizationState.UNAUTHORIZED);
    }

    public String getAuthorizationUrl(String discordId) {
        return googleFlow.newAuthorizationUrl()
            .setRedirectUri(AUTHORIZE_REDIRECT_URL)
            .setState(discordId)
            .build();
    }

    @Nullable
    public Credential getCredentials(String discordId) {
        try {
            Credential credential = googleFlow.loadCredential(discordId);
            if (isValid(credential))
                return credential;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void storeCredentials(String code, String discordId) {
        try {
            TokenResponse response = googleFlow.newTokenRequest(code).setRedirectUri(AUTHORIZE_REDIRECT_URL).execute();
            googleFlow.createAndStoreCredential(response, discordId);
            System.out.println("Created and stored credentials for discordId: " + discordId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isValid(@Nullable Credential credential) {
        if (credential == null)
            return false;
        if (credential.getRefreshToken() != null || credential.getExpiresInSeconds() == null || credential.getExpiresInSeconds() > 60)
            return true;
        try {
            // Try refresh the token
            return credential.refreshToken();
        } catch (IOException ignored) {
            return false;
        }
    }

    public static String toBase64(String str) {
        return Base64.getUrlEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }
}
