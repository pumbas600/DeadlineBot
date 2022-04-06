package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.models.Course;

import java.util.List;

public interface CourseService
{
    List<Course> listAllTrackedBy(String discordId);

    Course findById(String discordId, Long courseId);
}
