package net.pumbas.deadlinebot;

import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@RestController
@RequestMapping("/api/v1")
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

    @GetMapping("/authorize/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("You've successfully connected :)");
    }

    @PostMapping("/authorize")
    public ResponseEntity<String> addAuthorization(@RequestBody String content) {
        return ResponseEntity.badRequest().body(content);
    }
}
