package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.util.Key;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiscordAccessToken
{
    @Key("access_token")
    private final String accessToken;

    @Key("token_type")
    private final String tokenType;

    @Key("expires_in")
    private final long expiresIn;

    @Key("refresh_token")
    private final String refreshToken;

    private String scope;
}
