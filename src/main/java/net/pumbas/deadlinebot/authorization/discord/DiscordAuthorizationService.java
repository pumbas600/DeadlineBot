package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.AuthorizationService;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

@Service
public class DiscordAuthorizationService
{
    public static final List<String> DISCORD_SCOPES = List.of("identify");
    public static final String REDIRECT_URL = "http://localhost:8080" + App.API_PREFIX + "/authorize/discord/redirect";

    private final Map<String, UserData> userData = new ConcurrentHashMap<>();

    private String baseAuthorizationUrl;
    private DiscordCredentials discordCredentials;
    private JsonFactory jsonFactory;
    private HttpClient httpClient = HttpClient.newHttpClient();

    @Bean
    public CommandLineRunner initialiseDiscordService() {
        return (args) -> {
            this.jsonFactory = GsonFactory.getDefaultInstance();
            InputStream discordCredentialJson = new ClassPathResource("discord_credentials.json").getInputStream();
            this.discordCredentials = jsonFactory.fromInputStream(discordCredentialJson, DiscordCredentials.class);
            this.baseAuthorizationUrl = "https://discord.com/api/oauth2/authorize?client_id=" + this.discordCredentials.getClientId()  +
                "&redirect_uri=" + escapeUrlCharacters(REDIRECT_URL) +
                "&response_type=code&scope=" + String.join("%20", DISCORD_SCOPES);
        };
    }

    public CompletableFuture<UserData> exchangeCode(String sessionId, String code) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://discord.com/api/v8/oauth2/token"))
            .header("Content-Type", "application/x-www-form-urlencoded")
            .POST(BodyPublishers.ofString("""
                {
                    "client_id"="%s",
                    "client_secret"="%s",
                    "grant_type"="authorization_code",
                    "code"="%s",
                    "redirect_uri"="%s"
                }
                """.formatted(
                    this.discordCredentials.getClientId(),
                    this.discordCredentials.getSecretId(),
                    code,
                    "http://localhost:8080/authorization/authorize"
                )))
            .build();

        return this.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
            .thenApply((response) -> {
                try {
                    return this.jsonFactory.fromString(response.body(), DiscordAccessToken.class);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            })
            .thenCompose((accessToken) -> {
                if (accessToken == null)
                    return CompletableFuture.supplyAsync(() -> UserData.EMPTY);
                return this.updateUserData(sessionId, accessToken);
            });
    }

    public UserData getUserData(String sessionId) {
        return this.userData.getOrDefault(sessionId, UserData.EMPTY);
    }

    public CompletableFuture<UserData> updateUserData(String sessionId, DiscordAccessToken accessToken) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://discord.com/api/users/@me"))
            .header("authorization", "%s %s".formatted(accessToken.getTokenType(), accessToken.getAccessToken()))
            .build();

            return this.httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply((response) -> {
                    if (response.statusCode() == 200) { // Ok
                        try {
                            UserData userData = this.jsonFactory.fromString(response.body(), UserData.class);
                            this.userData.put(sessionId, userData);
                            return userData;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    return UserData.EMPTY;
                });
    }

    public String getAuthorizationUrl(HttpSession session) {
        return this.baseAuthorizationUrl + "&state=" + AuthorizationService.toBase64(session.getId());
    }

    public static String escapeUrlCharacters(String redirectUrl) {
        return redirectUrl.replaceAll("/", "%2F")
            .replaceAll("\\\\", "%5C")
            .replaceAll(":", "%3A");
    }
}
