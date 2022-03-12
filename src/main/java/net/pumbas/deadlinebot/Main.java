package net.pumbas.deadlinebot;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.GeneralSecurityException;

@SpringBootApplication
public class Main
{
    private final Credentials credentials;

    public Main(Credentials credentials) {
        this.credentials = credentials;
        this.initialise();
    }

    private void initialise() {
        try {
            System.out.println(credentials.getApiKey());

            Calendar calendar = new Calendar.Builder(GoogleNetHttpTransport.newTrustedTransport(), GsonFactory.getDefaultInstance(), null)
                .setApplicationName("Deadline Bot")
                .build();
        } catch (GeneralSecurityException | IOException e) {
            System.out.println("Caught the exception: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}
