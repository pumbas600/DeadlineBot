package net.pumbas.deadlinebot.models.calendar;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserData
{
    private final String linkedDiscordId;
    private final List<TrackedCalendar> trackedCalendars = new ArrayList<>();
}
