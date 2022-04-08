package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.repositories.CourseRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService
{
    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public List<Course> findAllTrackedBy(String discordId) {
        return null;
    }

    @Override
    public List<Course> findAllOwnedBy(String discordId) {
        return this.courseRepository.findCoursesOwnedBy(discordId);
    }

    @Override
    public Course findById(String courseId) throws ResourceNotFoundException {
        return this.courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("There is no course with the id " + courseId));
    }

    @Override
    public Course save(Course course) {
        return this.courseRepository.save(course);
    }

    @Override
    public List<Course> saveAll(Iterable<Course> courses) {
        return this.courseRepository.saveAll(courses);
    }

    @Override
    public void deleteById(String courseId) {
        this.courseRepository.deleteById(courseId);
    }

    @Override
    public void delete(Course course) {
        this.courseRepository.delete(course);
        // TODO: Delete references in User database
    }
}
