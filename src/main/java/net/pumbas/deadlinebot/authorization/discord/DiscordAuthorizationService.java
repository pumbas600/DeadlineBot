package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.AuthorizationService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpSession;

@Service
public class DiscordAuthorizationService
{
    public static final List<String> DISCORD_SCOPES = List.of("identify");
    public static final String REDIRECT_URL = "http://localhost:8080" + App.API_V1 + "/authorize/discord/redirect";

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

    public UserData exchangeCode(String sessionId, String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        LinkedMultiValueMap<String, String> properties = new LinkedMultiValueMap<>();
        properties.add("client_id", this.discordCredentials.getClientId());
        properties.add("client_secret", this.discordCredentials.getSecretId());
        properties.add("grant_type", "authorization_code");
        properties.add("code", code);
        properties.add("redirect_uri", "http://localhost:8080/api/v1/authorize/discord/redirect");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DiscordAccessToken> response = restTemplate.postForEntity(
            "https://discord.com/api/v8/oauth2/token",
            new HttpEntity<>(properties, headers),
            DiscordAccessToken.class);

        DiscordAccessToken accessToken = response.getBody();
        if (accessToken != null) {
            return this.updateUserData(sessionId, accessToken);
        }
        return UserData.EMPTY;
    }

    public UserData getUserData(String sessionId) {
        return this.userData.getOrDefault(sessionId, UserData.EMPTY);
    }

    public UserData updateUserData(String sessionId, DiscordAccessToken accessToken) {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://discord.com/api/users/@me"))
            .header("authorization", "%s %s".formatted(accessToken.getTokenType(), accessToken.getAccessToken()))
            .build();

            try {
                HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() == 200) {
                    UserData userData = this.jsonFactory.fromString(response.body(), UserData.class);
                    this.userData.put(sessionId, userData);
                    return userData;
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            }
            return UserData.EMPTY;
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
