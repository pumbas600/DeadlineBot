package net.pumbas.deadlinebot.canvascalendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.AuthorizationService;
import net.pumbas.deadlinebot.authorization.UnauthorizedException;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalendarService
{
    private final Map<String, CalendarModel> userCalendars = new ConcurrentHashMap<>();
    private final NetHttpTransport httpTransport;
    private final JsonFactory jsonFactory;
    private final AuthorizationService authorizationService;

    public CalendarService(AuthorizationService authorizationService) throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.jsonFactory = GsonFactory.getDefaultInstance();

        this.authorizationService = authorizationService;
    }

    /**
     * Attempts to automatically identify the imported canvas calendar, which is usually in the format <code>Firstname
     * Lastname Calendar (Canvas)</code>. If it can't find a canvas that matches this (Either because the canvas calendar
     * hasn't been imported or because it's been renamed to something else) then null will be returned.
     *
     * @param discordId
     *      The discord id of the user
     *
     * @return The first calendar with a summary ending in <code>Calendar (Canvas)</code> or null if there were none
     * @throws UnauthorizedException
     *      If the user hasn't been authorized
     * @throws IOException
     *      If there was an error fetching the calendars
     */
    @Nullable
    public CalendarModel attemptToIdentifyCanvasCalendar(String discordId) throws UnauthorizedException, IOException {
        Calendar service = this.getCalendar(discordId);
        CalendarList calendarList = service.calendarList().list().execute();
        return calendarList.getItems()
            .stream()
            .filter(calendar -> calendar.getSummary().trim().endsWith("Calendar (Canvas)"))
            .findFirst()
            .map(calendar -> new CalendarModel(calendar.getId(), calendar.getSummary()))
            .orElse(null);
    }

    private Calendar getCalendar(String discordId) throws UnauthorizedException {
        Credential credential = authorizationService.getCredentials(discordId);
        // The user hasn't authorized Deadline Bot
        if (credential == null)
            throw new UnauthorizedException("The user " + discordId + " is unauthorized!");

        return new Calendar.Builder(this.httpTransport, this.jsonFactory, credential)
            .setApplicationName(App.NAME)
            .build();
    }
}
