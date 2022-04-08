package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.repositories.course.CourseRepository;
import net.pumbas.deadlinebot.repositories.course.CourseTemplateRepository;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CourseServiceImpl implements CourseService
{
    private final static Pattern COURSE_NAME_PATTERN = Pattern.compile("([a-zA-Z]+) ?(\\w*[\\d]+\\w*)");

    private final CourseRepository courseRepository;
    private final CourseTemplateRepository courseTemplateRepository;

    public CourseServiceImpl(CourseRepository courseRepository, CourseTemplateRepository courseTemplateRepository) {
        this.courseRepository = courseRepository;
        this.courseTemplateRepository = courseTemplateRepository;
    }

    @Override
    public List<Course> findAllOwnedBy(String discordId) {
        return this.courseRepository.findCoursesOwnedBy(discordId);
    }

    @Override
    public List<Course> findAllWithName(String courseName) {
        return this.courseRepository.findPublicCoursesWithNameLike(courseName);
    }

    @Override
    public Course findById(String courseId) throws ResourceNotFoundException {
        return this.courseRepository.findById(courseId)
            .orElseThrow(() -> new ResourceNotFoundException("There is no course with the id " + courseId));
    }

    @Override
    public Course save(Course course) throws BadRequestException {
        this.formatCourseName(course);
        return this.courseRepository.save(course);
    }

    @Override
    public Course update(Course course) throws BadRequestException {
        this.formatCourseName(course);
        // TODO: If the course is being made private, delete any references to it
        return this.courseRepository.save(course);
    }

    @Override
    public void deleteById(String courseId, String discordId) throws ResourceNotFoundException {
        if (!this.courseTemplateRepository.deleteCourseWithIdAndOwnedBy(courseId, discordId))
            throw new ResourceNotFoundException("There is no course owned by you with the id " + courseId);
    }

    private void formatCourseName(Course course) throws BadRequestException {
        Matcher matcher = COURSE_NAME_PATTERN.matcher(course.getName());
        if (matcher.matches()) {
            course.setName("%s %s".formatted(matcher.group(1), matcher.group(2)).toUpperCase());
        }
        else throw new BadRequestException("The course name %s doesn't match the correct format: SOFTENG 281");
    }
}
