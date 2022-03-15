package net.pumbas.deadlinebot.canvascalendar;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.CalendarList;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.authorization.AuthorizationService;
import net.pumbas.deadlinebot.authorization.UnauthorizedAccessException;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalendarService
{
    private final Map<String, CalendarData> userCalendars = new ConcurrentHashMap<>();

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
     * @throws UnauthorizedAccessException
     *      If the user hasn't authorized the bot to access their calendar
     */
    @Nullable
    public CalendarData attemptToIdentifyCanvasCalendar(String discordId) throws UnauthorizedAccessException {
        return this.listCalendars(discordId)
            .stream()
            .filter(calendar -> calendar.getSummary().trim().endsWith("Calendar (Canvas)"))
            .findFirst()
            .orElse(null);
    }

    /**
     * Retrieves a list of all the calendars that the specified user has. If they have no calendars, or there was an
     * {@link IOException} while fetching them, an empty list is returned.
     *
     * @param discordId
     *      The discord id of the user
     *
     * @return An immutable list of all the calendars the specified user has
     * @throws UnauthorizedAccessException
     *      If the user hasn't authorized the bot to access their calendar
     */
    public List<CalendarData> listCalendars(String discordId) throws UnauthorizedAccessException {
        try {
            Calendar service = this.getCalendar(discordId);
            CalendarList calendarList = service.calendarList().list().execute();
            return calendarList.getItems()
                .stream()
                .map(calendar -> new CalendarData(calendar.getId(), calendar.getSummary()))
                .toList();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public List<Event> getEventsBefore(String discordId, String calendarId, OffsetDateTime until)
        throws UnauthorizedAccessException {
        Calendar service = this.getCalendar(discordId);
        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime end = offsetTimeToDateTime(until);

        try {
            Events events = service.events().list(calendarId)
                .setMaxResults(100)
                .setTimeMin(now)
                .setTimeMax(end)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
            return events.getItems();
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private Calendar getCalendar(String discordId) throws UnauthorizedAccessException {
        Credential credential = authorizationService.getCredentials(discordId);
        // The user hasn't authorized Deadline Bot
        if (credential == null)
            throw new UnauthorizedAccessException("The user " + discordId + " is unauthorized!");

        return new Calendar.Builder(this.httpTransport, this.jsonFactory, credential)
            .setApplicationName(App.NAME)
            .build();
    }

    public static DateTime offsetTimeToDateTime(OffsetDateTime offsetDateTime) {
        return new DateTime(offsetDateTime.toInstant().toEpochMilli());
    }
}
