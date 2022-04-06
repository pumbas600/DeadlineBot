package net.pumbas.deadlinebot.services.calendar;

import net.pumbas.deadlinebot.exceptions.MissingResourceException;
import net.pumbas.deadlinebot.exceptions.UnauthorizedAccessException;
import net.pumbas.deadlinebot.models.calendar.TrackedCalendar;
import net.pumbas.deadlinebot.models.calendar.TrackedEvent;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

public interface CalendarService
{
    TrackedCalendar findById(String discordId, String calendarId)
        throws UnauthorizedAccessException, MissingResourceException;

    List<TrackedCalendar> listAllOwnedBy(String discordId);

    List<TrackedEvent> listEventsBefore(String discordId, String calendarId, OffsetDateTime end)
        throws UnauthorizedAccessException, MissingResourceException;

    Set<String> listCourses(String discordId, String calendarId)
        throws UnauthorizedAccessException, MissingResourceException;

    TrackedCalendar save(TrackedCalendar trackedCalendar);

    @Nullable
    TrackedCalendar determineInitialCalendar(String discordId)
        throws UnauthorizedAccessException;
}
