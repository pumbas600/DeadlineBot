package net.pumbas.deadlinebot.services.user;

import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.models.PostUser;
import net.pumbas.deadlinebot.models.User;
import net.pumbas.deadlinebot.repositories.user.UserRepository;
import net.pumbas.deadlinebot.services.course.CourseService;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final CourseService courseService;

    public UserServiceImpl(UserRepository userRepository, CourseService courseService) {
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    @Override
    public User findById(String userId) throws ResourceNotFoundException {
        return this.userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("There is no user with the id " + userId));
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User save(PostUser newUser) throws BadRequestException {
        List<Course> courses = this.validateCourses(newUser.getDiscordId(), newUser.getCourseIds());
        User user = new User(newUser.getDiscordId(), newUser.getCalendarId(), courses);

        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(String userId) throws ResourceNotFoundException {
        if (this.userRepository.existsById(userId))
            this.userRepository.deleteById(userId);
        else throw new ResourceNotFoundException("There is no user with the id " + userId);
    }

    @Override
    public User updateCourses(String userId, List<String> courseId)
        throws ResourceNotFoundException, BadRequestException {
        User user = this.findById(userId);

        List<Course> courses = this.validateCourses(userId, courseId);

        user.setCourses(courses);
        return this.userRepository.save(user);
    }

    private List<Course> validateCourses(String discordId, List<String> courseIds) throws BadRequestException {
        List<Course> courses = this.courseService.findAllById(courseIds);
        boolean notAllTrackable = courses.size() != courseIds.size() ||
            courses.stream().noneMatch(course -> course.trackableBy(discordId));

        if (notAllTrackable)
            throw new BadRequestException("Not all the courses are trackable");
        return courses;
    }
}
