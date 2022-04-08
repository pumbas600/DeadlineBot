package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;

import java.util.List;

public interface CourseService
{
    List<Course> findAllTrackedBy(String discordId);

    List<Course> findAllOwnedBy(String discordId);

    Course findById(String courseId) throws ResourceNotFoundException;

    Course save(Course course);

    List<Course> saveAll(Iterable<Course> courses);

    void deleteById(String courseId, String discordId) throws ResourceNotFoundException;

    void delete(Course course);
}
