package net.pumbas.deadlinebot.services.course;

import net.pumbas.deadlinebot.Utils;
import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.repositories.course.CourseRepository;
import net.pumbas.deadlinebot.repositories.course.CourseTemplateRepository;
import net.pumbas.deadlinebot.repositories.user.UserTemplateRepository;

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
    private final UserTemplateRepository userTemplateRepository;

    public CourseServiceImpl(CourseRepository courseRepository,
                             CourseTemplateRepository courseTemplateRepository,
                             UserTemplateRepository userTemplateRepository
    ) {
        this.courseRepository = courseRepository;
        this.courseTemplateRepository = courseTemplateRepository;
        this.userTemplateRepository = userTemplateRepository;
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
    public List<Course> findAllById(Iterable<String> courseIds) {
        return Utils.listOf(this.courseRepository.findAllById(courseIds));
    }

    @Override
    public Course save(Course course) throws BadRequestException {
        this.formatCourseName(course);
        return this.courseRepository.save(course);
    }

    @Override
    public Course update(Course course) throws BadRequestException {
        this.formatCourseName(course);

        Course updatedCourse = this.courseRepository.save(course);
        if (!course.isPublic())
            this.userTemplateRepository.deletePublicCourseReferences(course.getOwnerId(), course.getId());

        return updatedCourse;
    }

    @Override
    public void deleteById(String courseId, String discordId) throws ResourceNotFoundException {
        if (!this.courseTemplateRepository.deleteCourseWithIdAndOwnedBy(courseId, discordId))
            throw new ResourceNotFoundException("There is no course owned by you with the id " + courseId);
        this.userTemplateRepository.deleteAllCourseReferences(courseId);
    }

    private void formatCourseName(Course course) throws BadRequestException {
        Matcher matcher = COURSE_NAME_PATTERN.matcher(course.getName());
        if (matcher.matches()) {
            course.setName("%s %s".formatted(matcher.group(1), matcher.group(2)).toUpperCase());
        }
        else throw new BadRequestException(
            "The course name %s doesn't match the correct format: SOFTENG 281".formatted(course.getName()));
    }
}
