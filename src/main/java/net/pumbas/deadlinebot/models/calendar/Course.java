package net.pumbas.deadlinebot.models.calendar;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Course
{
    private Long courseId; // Generated
    private final String courseName;
    private final boolean isPublic;

    public Course(String courseName, boolean isPublic) {
        this.courseName = courseName;
        this.isPublic = isPublic;
    }
}
