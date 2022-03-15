package net.pumbas.deadlinebot.canvascalendar;

import com.google.api.services.calendar.model.Event;

import net.pumbas.deadlinebot.App;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;
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
    public ResponseEntity<CalendarData> getCanvasCalendar(@RequestParam(name = "id") String discordId) {
        System.out.println("Attempting to find canvas calendar!");
        CalendarData calendar = this.calendarService.attemptToIdentifyCanvasCalendar(discordId);
        return ResponseEntity.ok(calendar);
    }

    @GetMapping("/upcoming/week")
    public ResponseEntity<List<Event>> getUpcomingEventsNextWeek(@RequestParam(name = "id") String discordId) {
        CalendarData calendar = this.calendarService.attemptToIdentifyCanvasCalendar(discordId);
        if (calendar == null)
            return ResponseEntity.badRequest().build();
        List<Event> events =  this.calendarService
            .getEventsBefore(discordId, calendar.getId(), OffsetDateTime.now().plusWeeks(1));
        return ResponseEntity.ok(events);
    }
}
