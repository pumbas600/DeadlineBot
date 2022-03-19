package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscordAccessToken
{
    @Key("access_token")
    private String accessToken;

    @Key("token_type")
    private String tokenType;

    @Key("expires_in")
    private long expiresIn;

    @Key("refresh_token")
    private String refreshToken;

    private String scope;
}
