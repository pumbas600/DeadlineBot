package net.pumbas.deadlinebot.controllers;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.models.PostUser;
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

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{discordId}")
    public User getUser(@PathVariable String discordId) {
        return this.userService.findById(discordId);
    }

    @PostMapping("/users")
    public User newUser(@RequestBody PostUser newUser, @RequestParam("discord_id") String discordId) {
        newUser.setDiscordId(discordId);
        return this.userService.save(newUser);
    }

    @PutMapping("/users/{discordId}")
    public User updateUser(@PathVariable String discordId, @RequestBody PostUser newUser) {
        newUser.setDiscordId(discordId);
        return this.userService.save(newUser);
    }

    @PutMapping("/users/{discordId}/courses")
    public User updateUserCourses(@PathVariable String discordId, @RequestBody List<String> courseIds) {
        return this.userService.updateCourses(discordId, courseIds);
    }

    @DeleteMapping("/users/{discordId}")
    public void deleteCourse(@PathVariable String discordId) {
        this.userService.deleteById(discordId);
    }
}
