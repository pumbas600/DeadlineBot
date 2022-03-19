package net.pumbas.deadlinebot.authorization.discord;

import com.fasterxml.jackson.annotation.JsonAlias;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DiscordAccessToken
{
    @JsonAlias("access_token")
    private String accessToken;

    @JsonAlias("token_type")
    private String tokenType;

    @JsonAlias("expires_in")
    private long expiresIn;

    @JsonAlias("refresh_token")
    private String refreshToken;

    private String scope;
}
