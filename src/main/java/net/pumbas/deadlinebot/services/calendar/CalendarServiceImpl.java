package net.pumbas.deadlinebot.services.calendar;

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
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.exceptions.UnauthorizedAccessException;
import net.pumbas.deadlinebot.models.calendar.CalendarData;
import net.pumbas.deadlinebot.models.calendar.TrackedCalendar;
import net.pumbas.deadlinebot.models.calendar.TrackedEvent;
import net.pumbas.deadlinebot.models.calendar.UserData;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class CalendarServiceImpl implements CalendarService
{
    private static final Pattern COURSE_EVENT_PATTERN = Pattern.compile(".*\\[(.+)]");

    private final Map<String, TrackedCalendar> trackedCalendars = new ConcurrentHashMap<>();

    private final NetHttpTransport httpTransport;
    private final JsonFactory jsonFactory;
    private final AuthorizationService authorizationService;

    public CalendarServiceImpl(AuthorizationService authorizationService)
        throws GeneralSecurityException, IOException {
        this.httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        this.jsonFactory = GsonFactory.getDefaultInstance();

        this.authorizationService = authorizationService;
    }

    @Override
    public TrackedCalendar findById(String discordId, String calendarId)
        throws UnauthorizedAccessException, ResourceNotFoundException {
        if (!this.trackedCalendars.containsKey(calendarId))
            throw new ResourceNotFoundException("There is no tracked calendar with the id " + calendarId);

        TrackedCalendar trackedCalendar = this.trackedCalendars.get(calendarId);
        if (trackedCalendar.getOwnerId().equals(discordId) || trackedCalendar.isPublic())
            return trackedCalendar;

        throw new UnauthorizedAccessException(
            "The user %s cannot access the calendar %s".formatted(discordId, calendarId));

    }

    @Override
    public List<TrackedCalendar> listAllOwnedBy(String discordId) {
        return this.trackedCalendars.values()
            .stream()
            .filter(trackedCalendar -> trackedCalendar.getOwnerId().equals(discordId))
            .toList();
    }

    @Override
    public List<TrackedEvent> listEventsBefore(
        String discordId,
        String calendarId,
        OffsetDateTime end
    ) throws UnauthorizedAccessException, ResourceNotFoundException {
        TrackedCalendar trackedCalendar = this.findById(discordId, calendarId);

        return this.fetchEventsBefore(discordId, calendarId, end)
            .stream()
            .filter(event -> trackedCalendar.isTracked(event.getSummary()))
            .map(TrackedEvent::from)
            .toList();
    }

    @Override
    public Set<String> listCourses(String discordId, String calendarId)
        throws UnauthorizedAccessException, ResourceNotFoundException {
        TrackedCalendar trackedCalendar = this.findById(discordId, calendarId);
        return trackedCalendar.getCourses();
    }

    @Override
    public TrackedCalendar save(TrackedCalendar trackedCalendar) {
        return this.trackedCalendars.put(trackedCalendar.getId(), trackedCalendar);
    }

    @Nullable
    @Override
    public TrackedCalendar determineInitialCalendar(String discordId) throws UnauthorizedAccessException {
        CalendarData calendarData = this.attemptToIdentifyCanvasCalendar(discordId);
        if (calendarData != null) {
            Set<String> courses = this.identifyCourses(discordId, calendarData.getId());
            TrackedCalendar trackedCalendar = new TrackedCalendar(discordId, calendarData);
            trackedCalendar.addCourses(courses);
        }
        return null;
    }

    public UserData generateInitialUserData(String discordId) {
        UserData userData = new UserData(discordId);
        CalendarData calendarData = this.attemptToIdentifyCanvasCalendar(discordId);
        if (calendarData != null) {
            Set<String> courses = this.identifyCourses(discordId, calendarData.getId());
            TrackedCalendar trackedCalendar = new TrackedCalendar(discordId, calendarData);
            trackedCalendar.addCourses(courses);

            userData.getTrackedCalendars().add(trackedCalendar);
        }
        return userData;
    }

    private Set<String> identifyCourses(String discordId, String calendarId) {
        return this.listEventsBefore(discordId, calendarId, OffsetDateTime.now().plusMonths(1))
            .stream()
            .map(event -> {
                Matcher matcher = COURSE_EVENT_PATTERN.matcher(event.getSummary());
                if (matcher.matches()) {
                    return matcher.group(1);
                }
                return null;
            })
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    /**
     * Attempts to automatically identify the imported canvas calendar, which is usually in the format <code>Firstname
     * Lastname Calendar (Canvas)</code>. If it can't find a canvas that matches this (Either because the canvas
     * calendar hasn't been imported or because it's been renamed to something else) then null will be returned.
     *
     * @param discordId
     *     The discord id of the user
     *
     * @return The first calendar with a summary ending in <code>Calendar (Canvas)</code> or null if there were none
     * @throws UnauthorizedAccessException
     *     If the user hasn't authorized the bot to access their calendar
     */
    @Nullable
    private CalendarData attemptToIdentifyCanvasCalendar(String discordId)
        throws UnauthorizedAccessException {
        return this.listGoogleCalendars(discordId)
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
     *     The discord id of the user
     *
     * @return An immutable list of all the calendars the specified user has
     * @throws UnauthorizedAccessException
     *     If the user hasn't authorized the bot to access their calendar
     */
    public List<CalendarData> listGoogleCalendars(String discordId)
        throws UnauthorizedAccessException {
        try {
            Calendar service = this.getService(discordId);
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

    /**
     * Retrieves all the {@link Event events} in the specified calendar for the user between now and the end date. If
     * there  are no events before the end date, or there was an {@link IOException} while fetching them, an empty list
     * is returned instead.
     *
     * @param discordId
     *     The discord id of the user
     * @param calendarId
     *     The id of the calendar to fetch the events from
     * @param until
     *     The date to fetch the events before
     *
     * @return An immutable list of the {@link Event events} between now and the specified end date
     * @throws UnauthorizedAccessException
     *     If the user hasn't authorized the bot to access their calendar
     */
    public List<Event> fetchEventsBefore(String discordId, String calendarId, OffsetDateTime until)
        throws UnauthorizedAccessException {
        Calendar service = this.getService(discordId);
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

    private Calendar getService(String discordId)
        throws UnauthorizedAccessException {
        Credential credential = this.authorizationService.getCredentials(discordId);
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
