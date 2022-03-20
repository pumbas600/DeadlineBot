package net.pumbas.deadlinebot.canvascalendar;

import net.pumbas.deadlinebot.App;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(App.API_PREFIX)
public class CalendarRestController
{
    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/canvas_calendar")
    public ResponseEntity<TrackedCalendar> getCanvasCalendar(
        @RequestParam(name = "id") String discordId,
        HttpSession session
    ) {

        System.out.println("Session id: " + session.getId());
        TrackedCalendar calendar = this.calendarService.findById(discordId);
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/google/{discordId}/calendars")
    public List<CalendarData> getGoogleCalendars(@PathVariable String discordId) {
        return this.calendarService.listGoogleCalendars(discordId);
    }

    @GetMapping("/deadlines/calendar/{id}")
    public ResponseEntity<TrackedCalendar> getTrackedCalendar(@PathVariable String id) {
        TrackedCalendar trackedCalendar = this.calendarService.findById(id);
        if (trackedCalendar == null)
            return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(trackedCalendar);
    }

    @GetMapping("/deadlines/{discordId}/calendars/")
    public List<TrackedCalendar> getTrackedCalendars(@PathVariable String discordId) {
        return this.calendarService.listTrackedCalendars(discordId);
    }

    @PostMapping("/deadlines/calendars")
    public TrackedCalendar newTrackedCalendar(@RequestBody TrackedCalendar trackedCalendar) {
        // TODO: Check ownerId is authorized
        return this.calendarService.save(trackedCalendar);
    }

    @GetMapping("/upcoming/week")
    public ResponseEntity<List<TrackedEvent>> getUpcomingEventsNextWeek(
        @RequestParam(name = "id") String discordId,
        @RequestParam(name = "blacklist", defaultValue = "") String blacklistedSubjects,
        HttpSession session
    ) {
        System.out.println("Session id: " + session.getId());
        TrackedCalendar calendar = this.calendarService.findById(discordId);
        if (calendar == null)
            return ResponseEntity.badRequest().build();

        calendar.clearCourses();
        calendar.addCourses(Set.of(blacklistedSubjects.split(",")));
        OffsetDateTime end = OffsetDateTime.now().plusWeeks(1);

        List<TrackedEvent> events =  this.calendarService.listTrackedEventsBefore(discordId, calendar, end);
        return ResponseEntity.ok(events);
    }
}
