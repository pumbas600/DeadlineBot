package net.pumbas;

import com.google.api.client.util.Key;

import lombok.Getter;

@Getter
public class Credentials
{
    @Key
    private String apiKey;

    @Key
    private String clientID;

    @Key
    private String clientSecret;
}
