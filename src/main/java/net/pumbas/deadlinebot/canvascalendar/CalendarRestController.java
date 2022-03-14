package net.pumbas.deadlinebot.canvascalendar;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.common.Message;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(App.API_PREFIX)
public class CalendarRestController
{
    private final CalendarService calendarService;

    public CalendarRestController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @GetMapping("/canvas_calendar")
    public ResponseEntity<Object> getCanvasCalendar(@RequestParam(name = "id") String discordId) {
        System.out.println("Attempting to find canvas calendar!");
        try {
            CalendarModel calendar = calendarService.attemptToIdentifyCanvasCalendar(discordId);
            return ResponseEntity.ok(calendar);
        }
        catch (IOException e) {
            return ResponseEntity.badRequest().body(new Message(e.getMessage()));
        }
    }
}
