package net.pumbas;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class Main
{
    private static final JsonFactory jsonFactory = new GsonFactory();

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        System.out.println("Hello world!");

        File credentialsFile = new File("credentials.json");
        if (!credentialsFile.exists()) {
            System.out.println("Expected %s file!".formatted(credentialsFile.getAbsolutePath()));
            return;
        }

        Credentials credentials = jsonFactory.fromInputStream(
            new FileInputStream(credentialsFile), Credentials.class);

        System.out.println(credentials.getApiKey());

        Calendar calendar = new Calendar(GoogleNetHttpTransport.newTrustedTransport(), jsonFactory, null);
    }
}
