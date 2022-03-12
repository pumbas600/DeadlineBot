package net.pumbas.deadlinebot;

import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@Service
public class OAuthAuthorizationController
{
    private final String END_POINT = "https://pumbas.net/authorize";

    @Bean
    public Credentials credentialsProvider(Gson json) throws ApplicationException, FileNotFoundException {
        File credentialsFile = new File("credentials.json");
        if (!credentialsFile.exists()) {
            throw new ApplicationException("Expected %s file!".formatted(credentialsFile.getAbsolutePath()));
        }

        return json.fromJson(new InputStreamReader(new FileInputStream(credentialsFile)), Credentials.class);
    }

}
