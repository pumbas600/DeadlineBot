package net.pumbas.deadlinebot.controllers;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.services.course.CourseService;

import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(App.API_V1)
public class CourseController
{
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("courses/")
    public List<Course> getCourses(@RequestHeader(HttpHeaders.AUTHORIZATION) String discordId) {
        return this.courseService.findAllTrackedBy(discordId);
    }
}
