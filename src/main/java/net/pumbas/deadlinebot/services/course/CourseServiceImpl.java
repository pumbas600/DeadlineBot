package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.repositories.CourseRepository;
import net.pumbas.deadlinebot.repositories.CourseTemplateRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService
{
    private final CourseRepository courseRepository;
    private final CourseTemplateRepository courseTemplateRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CourseTemplateRepository courseTemplateRepository) {
        this.courseRepository = courseRepository;
        this.courseTemplateRepository = courseTemplateRepository;
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
        // TODO: Verify the courses matches the correct format and is capitalised
        return this.courseRepository.save(course);
    }

    @Override
    public List<Course> saveAll(Iterable<Course> courses) {
        return this.courseRepository.saveAll(courses);
    }

    @Override
    public void deleteById(String courseId, String discordId) throws ResourceNotFoundException {
        if (!this.courseTemplateRepository.deleteCourseWithIdAndOwnedBy(courseId, discordId))
            throw new ResourceNotFoundException("There is no course owned by you with the id " + courseId);
    }

    @Override
    public void delete(Course course) {
        this.courseRepository.delete(course);
        // TODO: Delete references in User database -
    }
}
