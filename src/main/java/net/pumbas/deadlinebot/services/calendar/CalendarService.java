package net.pumbas.deadlinebot.services.calendar;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
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
        throws UnauthorizedAccessException, ResourceNotFoundException;

    List<TrackedCalendar> listAllOwnedBy(String discordId);

    List<TrackedEvent> listEventsBefore(String discordId, String calendarId, OffsetDateTime end)
        throws UnauthorizedAccessException, ResourceNotFoundException;

    Set<String> listCourses(String discordId, String calendarId)
        throws UnauthorizedAccessException, ResourceNotFoundException;

    TrackedCalendar save(TrackedCalendar trackedCalendar);

    @Nullable
    TrackedCalendar determineInitialCalendar(String discordId)
        throws UnauthorizedAccessException;
}
