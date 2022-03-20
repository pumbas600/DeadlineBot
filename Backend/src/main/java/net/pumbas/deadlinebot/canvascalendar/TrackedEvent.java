package net.pumbas.deadlinebot.canvascalendar;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrackedEvent
{
    private static final Pattern ASSIGNMENT_PATTERN = Pattern.compile("(.*) \\[(.*)]");

    private final String summary;
    private final String name;
    private final String subject;
    private final DateTime start;
    private final DateTime end;
    private final boolean isAllDay;

    public TrackedEvent(String summary, DateTime start, DateTime end) {
        this.summary = summary;
        this.start = start;
        this.end = end;
        this.isAllDay = start.isDateOnly();

        Matcher matcher = ASSIGNMENT_PATTERN.matcher(summary);
        if (matcher.matches()) {
            this.name = matcher.group(1);
            this.subject = matcher.group(2);
        }
        else {
            this.name = "";
            this.subject = "";
        }
    }

    public static TrackedEvent from(Event event) {
        DateTime start = event.getEnd().getDateTime();
        DateTime end;
        if (start == null) { // All day event
            start = event.getStart().getDate();
            end = event.getEnd().getDate();
        }
        else end = event.getEnd().getDateTime();

        return new TrackedEvent(event.getSummary(), start, end);
    }
}
