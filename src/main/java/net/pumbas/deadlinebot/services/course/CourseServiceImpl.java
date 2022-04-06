package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.models.calendar.Course;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService
{
    @Override
    public List<Course> listAllTrackedBy(String discordId) {
        return null;
    }

    @Override
    public Course findById(String discordId, Long courseId) {
        return null;
    }
}
