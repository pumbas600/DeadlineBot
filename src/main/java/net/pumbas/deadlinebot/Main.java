package net.pumbas.deadlinebot;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@SpringBootApplication
public class Main
{

    private static final String TOKENS_FILE_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);

    private static Credential getCredentials(final NetHttpTransport httpTransport) throws IOException {
        InputStream in = new ClassPathResource(CREDENTIALS_FILE_PATH).getInputStream();
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(GsonFactory.getDefaultInstance(), new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
            httpTransport, GsonFactory.getDefaultInstance(), clientSecrets, SCOPES)
            .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_FILE_PATH)))
            .setAccessType("online")
            .build();

        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri("http://localhost:8080/api/v1/authorize/token");
        String url = authorizationUrl.build();
        System.out.printf("Go to %s to authorize\n", url);

        //TODO: Manually generate authorization string and exchange code
        LocalServerReceiver receiver = new LocalServerReceiver.Builder()
            .setHost("localhost")
            .setPort(8888)
            .setCallbackPath("/api/v1/authorize")
            .build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        SpringApplication.run(Main.class, args);
//        final NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
//        Calendar calendar = new Calendar.Builder(httpTransport, GsonFactory.getDefaultInstance(), getCredentials(httpTransport))
//            .setApplicationName("Deadline Bot")
//            .build();
//
//        DateTime now = new DateTime(System.currentTimeMillis());
//
//        Events events = calendar.events().list("primary")
//            .setMaxResults(10)
//            .setTimeMin(now)
//            .setOrderBy("startTime")
//            .setSingleEvents(true)
//            .execute();
//
//        List<Event> items = events.getItems();
//        if (items.isEmpty())
//            System.out.println("No upcoming events found");
//        else {
//            System.out.printf("Found %d upcoming events:\n", items.size());
//            for (Event event : items) {
//                DateTime start = event.getStart().getDateTime();
//                if (start == null)
//                    start = event.getStart().getDate(); // This is an all day event
//                System.out.printf("%s (%s)\n", event.getSummary(), start);
//            }
//        }
    }
}
