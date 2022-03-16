package net.pumbas.deadlinebot.canvascalendar;

import com.google.api.services.calendar.model.Event;

import net.pumbas.deadlinebot.App;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(App.API_PREFIX)
public class CalendarRestController
{
    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/canvas_calendar")
    public ResponseEntity<TrackedCalendar> getCanvasCalendar(@RequestParam(name = "id") String discordId) {
        System.out.println("Attempting to find canvas calendar!");
        TrackedCalendar calendar = this.calendarService.getCalendar(discordId);
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/upcoming/week")
    public ResponseEntity<List<TrackedEvent>> getUpcomingEventsNextWeek(
        @RequestParam(name = "id") String discordId,
        @RequestParam(name = "blacklist", defaultValue = "") String blacklistedSubjects
    ) {
        TrackedCalendar calendar = this.calendarService.getCalendar(discordId);
        if (calendar == null)
            return ResponseEntity.badRequest().build();

        for (String subject : blacklistedSubjects.split(",\s*")) {
            calendar.addBlacklistedSubject(subject);
        }
        OffsetDateTime end = OffsetDateTime.now().plusWeeks(1);

        List<TrackedEvent> events =  this.calendarService.listTrackedEventsBefore(discordId, calendar, end);
        return ResponseEntity.ok(events);
    }
}
