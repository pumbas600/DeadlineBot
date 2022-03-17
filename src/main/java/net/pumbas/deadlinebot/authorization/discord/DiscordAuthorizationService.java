package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import net.pumbas.deadlinebot.authorization.AuthorizationService;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpSession;

@Service
public class DiscordAuthorizationService
{
    public static final List<String> DISCORD_SCOPES = List.of("identity");

    private String baseAuthorizationUrl;
    private DiscordCredentials discordCredentials;

    @Bean
    public CommandLineRunner initialise() {
        return (args) -> {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            InputStream discordCredentialJson = new ClassPathResource("discord_credentials.json").getInputStream();
            this.discordCredentials = jsonFactory.fromInputStream(discordCredentialJson, DiscordCredentials.class);

            this.baseAuthorizationUrl = "https://discord.com/api/oauth2/authorize?client_id=" + this.discordCredentials.getClientId()  +
                "redirect_uri=" + escapeUrlCharacters(AuthorizationService.AUTHORIZE_REDIRECT_URL) +
                "&response_type=token&scope=" + String.join("%20", DISCORD_SCOPES);
        };
    }

    public String getId(String token) {
        return "";
        //TODO: Do post request using token to get id
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
