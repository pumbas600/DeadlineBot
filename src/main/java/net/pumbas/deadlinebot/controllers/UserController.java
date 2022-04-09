package net.pumbas.deadlinebot.controllers;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.models.User;
import net.pumbas.deadlinebot.services.course.CourseService;
import net.pumbas.deadlinebot.services.user.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(App.API_V1)
public class UserController
{
    private final UserService userService;
    private final CourseService courseService;

    public UserController(UserService userService, CourseService courseService) {
        this.userService = userService;
        this.courseService = courseService;
    }

    @GetMapping("/users/{discordId}")
    public User getUser(@PathVariable String discordId) {
        return this.userService.findById(discordId);
    }

    @PostMapping("/users")
    public User newUser(@RequestBody User user, @RequestParam("discord_id") String discordId) {
        user.setDiscordId(discordId);
        return this.userService.save(user);
    }

    @PutMapping("/users/{discordId}")
    public User updateUser(@PathVariable String discordId, @RequestBody User newUser) {
        newUser.setDiscordId(discordId);
        // TODO: Validate courses
        return this.userService.save(newUser);
    }

    @PutMapping("/users/{discordId}/courses")
    public User updateUserCourses(@PathVariable String discordId, @RequestBody List<String> courseIds) {
        User user = this.userService.findById(discordId);

        List<Course> courses = this.courseService.findAllById(courseIds);
        if (courses.stream().allMatch(course -> course.trackableBy(discordId)))
            throw new BadRequestException("Not all the courses are trackable");

        user.setCourses(courses);
        return this.userService.update(user);
    }

    @DeleteMapping("/users/{discordId}")
    public void deleteCourse(@PathVariable String discordId) {
        this.userService.deleteById(discordId);
    }
}
