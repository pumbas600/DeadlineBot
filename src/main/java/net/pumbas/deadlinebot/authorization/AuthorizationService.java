package net.pumbas.deadlinebot.authorization;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
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
import java.util.Collections;
import java.util.List;

@Service
public class AuthorizationService
{
    public static final String AUTHORIZE_REDIRECT_URL = "http://localhost:8080" + App.API_PREFIX + "/authorize/token";

    public static final String TOKENS_FILE_PATH = "tokens";
    public static final String CREDENTIALS_FILE_PATH = "credentials.json";
    public static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);

    private AuthorizationCodeFlow flow;
    private String authorizationUrl;

    @Bean
    public CommandLineRunner initialise() {
        return (args) -> {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            InputStream in = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();
            GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, new InputStreamReader(in));

            this.flow = new GoogleAuthorizationCodeFlow.Builder(
                GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_FILE_PATH)))
                .setAccessType("offline")
                .build();
        };
    }


    public String getAuthorizationUrl(String discordId) {
        if (authorizationUrl == null) {
            AuthorizationCodeRequestUrl authorizationRequestUrl = flow.newAuthorizationUrl()
                .setRedirectUri(AUTHORIZE_REDIRECT_URL)
                .setState(discordId);
            authorizationUrl = authorizationRequestUrl.build();
        }
        return authorizationUrl;
    }

    @Nullable
    public Credential getCredentials(String discordId) {
        try {
            Credential credential = flow.loadCredential(discordId);
            if (isValid(credential))
                return credential;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void storeCredentials(String code, String discordId) {
        try {
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(AUTHORIZE_REDIRECT_URL).execute();
            flow.createAndStoreCredential(response, discordId);
            System.out.println("Created and stored credentials for discordId:" + discordId);
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
}
