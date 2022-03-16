package net.pumbas.deadlinebot.canvascalendar;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TrackedCalendar
{
    private final String id;
    private final String summary;
    private final List<String> blacklistedClasses;

    public TrackedCalendar(String id, String summary) {
        this.id = id;
        this.summary = summary;
        this.blacklistedClasses = new ArrayList<>();
    }
}
