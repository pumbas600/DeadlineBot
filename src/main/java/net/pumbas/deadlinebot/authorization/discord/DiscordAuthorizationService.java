package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;

import net.pumbas.deadlinebot.App;
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
    public static final List<String> DISCORD_SCOPES = List.of("identify");
    public static final String REDIRECT_URL = "http://localhost:8080" + App.API_PREFIX + "/authorize/discord/redirect";

    private String baseAuthorizationUrl;
    private DiscordCredentials discordCredentials;

    @Bean
    public CommandLineRunner initialiseDiscordService() {
        return (args) -> {
            JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            InputStream discordCredentialJson = new ClassPathResource("discord_credentials.json").getInputStream();
            this.discordCredentials = jsonFactory.fromInputStream(discordCredentialJson, DiscordCredentials.class);
            this.baseAuthorizationUrl = "https://discord.com/api/oauth2/authorize?client_id=" + this.discordCredentials.getClientId()  +
                "&redirect_uri=" + escapeUrlCharacters(REDIRECT_URL) +
                "&response_type=code&scope=" + String.join("%20", DISCORD_SCOPES);

            String com = "https://discord.com/api/oauth2/authorize?client_id=953822247456477254&redirect_uri=http%3A" +
                "%2F%2Flocalhost%3A8080%2Fapi%2Fv1%2Fauthorize%2Fdiscord%2Fredirect&response_type=code&scope=identify";
            System.out.println(com.equals(this.baseAuthorizationUrl));
            System.out.println(baseAuthorizationUrl + "|");
            System.out.println(com + "|");
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
