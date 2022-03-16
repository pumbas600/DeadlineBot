package net.pumbas.deadlinebot.canvascalendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TrackedCalendar
{
    private final static Pattern SUBJECT_PATTERN = Pattern.compile("(\\w+) ?(\\d+)");

    @Getter
    private final String id;

    @Getter
    private final String summary;

    private final List<String> blacklistedSubjects;

    public TrackedCalendar(CalendarData calendarData) {
        this(calendarData.getId(), calendarData.getSummary());
    }

    public TrackedCalendar(String id, String summary) {
        this.id = id;
        this.summary = summary;
        this.blacklistedSubjects = new ArrayList<>();
    }

    public boolean isBlacklisted(String eventSummary) {
        return blacklistedSubjects.stream()
            .anyMatch(subject -> eventSummary.endsWith(subject + "]"));
    }

    public void addBlacklistedSubject(String subject) {

    }

    public List<String> getBlacklistedSubjects() {
        return Collections.unmodifiableList(blacklistedSubjects);
    }
}
