package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;

import java.util.List;

public interface CourseService
{
    List<Course> findAllOwnedBy(String discordId);

    List<Course> findAllWithName(String courseName);

    Course findById(String courseId) throws ResourceNotFoundException;

    Course save(Course course) throws BadRequestException;

    Course update(Course course) throws BadRequestException;

    void deleteById(String courseId, String discordId) throws ResourceNotFoundException;
}
