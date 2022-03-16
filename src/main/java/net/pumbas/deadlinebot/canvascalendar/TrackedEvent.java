package net.pumbas.deadlinebot.canvascalendar;

import com.google.api.client.util.DateTime;

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
    private final DateTime dateTime;

    public TrackedEvent(String summary, DateTime dateTime) {
        this.summary = summary;
        this.dateTime = dateTime;

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
}
