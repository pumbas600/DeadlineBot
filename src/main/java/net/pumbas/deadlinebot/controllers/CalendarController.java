package net.pumbas.deadlinebot.controllers;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.exceptions.MissingResourceException;
import net.pumbas.deadlinebot.exceptions.UnauthorizedAccessException;
import net.pumbas.deadlinebot.services.calendar.CalendarService;
import net.pumbas.deadlinebot.models.calendar.TrackedCalendar;
import net.pumbas.deadlinebot.models.calendar.TrackedEvent;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping(App.API_V1)
public class CalendarController
{
    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // For testing
    @GetMapping("/calendars/canvas")
    public ResponseEntity<TrackedCalendar> getCanvasCalendar() {
        String discordId = "260930648330469387";
        TrackedCalendar calendar = this.calendarService.determineInitialCalendar(discordId);
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/calendars/{calendarId}")
    public ResponseEntity<TrackedCalendar> getTrackedCalendar(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String discordId,
        @PathVariable String calendarId
    ) {
        try {
            TrackedCalendar trackedCalendar = this.calendarService.findById(discordId, calendarId);
            return ResponseEntity.ok(trackedCalendar);
        } catch (UnauthorizedAccessException | MissingResourceException e) {
            // Don't tell the user that the calendar doesn't exist. They don't need to know
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/calendars")
    public List<TrackedCalendar> getTrackedCalendars(@RequestHeader(HttpHeaders.AUTHORIZATION) String discordId) {
        return this.calendarService.listAllOwnedBy(discordId);
    }

    @PostMapping("/calendars")
    public TrackedCalendar newTrackedCalendar(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String discordId,
        @RequestBody TrackedCalendar trackedCalendar
    ) {
        return this.calendarService.save(trackedCalendar);
    }

    @GetMapping("/calendars/{calendarId}/upcoming/week")
    public ResponseEntity<List<TrackedEvent>> getUpcomingEventsNextWeek(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String discordId,
        @PathVariable String calendarId
    ) {
        try {
            OffsetDateTime end = OffsetDateTime.now().plusWeeks(1);
            List<TrackedEvent> events = this.calendarService.listEventsBefore(discordId, calendarId, end);
            return ResponseEntity.ok(events);
        } catch (UnauthorizedAccessException | MissingResourceException e) {
            // Don't tell the user that the calendar doesn't exist. They don't need to know
            return ResponseEntity.badRequest().build();
        }
    }
}
