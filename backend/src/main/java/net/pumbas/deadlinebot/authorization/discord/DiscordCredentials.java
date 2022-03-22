package net.pumbas.deadlinebot.authorization.discord;

import com.google.api.client.util.Key;

import lombok.Getter;

@Getter
public class DiscordCredentials
{
    @Key
    private String clientId;

    @Key
    private String secretId;
}
