package net.pumbas.deadlinebot.controllers;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.exceptions.UnauthorizedAccessException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.models.responses.Response;
import net.pumbas.deadlinebot.services.course.CourseService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(App.API_V1)
public class CourseController
{
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses")
    public ResponseEntity<Response> getAllCourses(@RequestParam("discord_id") String discordId) {
        return Response.success(this.courseService.findAllOwnedBy(discordId));
    }

    @GetMapping("/courses/like")
    public ResponseEntity<Response> getCoursesWithName(@RequestParam("name") String courseName) {
        return Response.success(this.courseService.findAllWithName(courseName));
    }

    @PostMapping("/courses")
    public ResponseEntity<Response> createCourse(@RequestBody Course course, @RequestParam("discord_id") String discordId) {
        course.setOwnerId(discordId);
        course.setId(null); // Automatically generate the course id
        return Response.success(this.courseService.save(course));
    }

    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Response> getCourse(@PathVariable String courseId, @RequestParam("discord_id") String discordId) {
        Course course = this.courseService.findById(courseId);
        if (course.trackableBy(discordId))
            return Response.success(course);
        throw new UnauthorizedAccessException("The course with the id %s is not public".formatted(courseId));
    }

    @PutMapping("/courses/{courseId}")
    public ResponseEntity<Response> updateCourse(@PathVariable String courseId,
                               @RequestBody Course newCourse,
                               @RequestParam("discord_id") String discordId
    ) {
        newCourse.setOwnerId(discordId);
        try {
            Course course = this.courseService.findById(courseId);
            if (course.isOwner(discordId)) {
                newCourse.setId(course.getId());
                return Response.success(this.courseService.update(newCourse));
            }
            else throw new UnauthorizedAccessException("You aren't the owner of the course %s so you cannot update it"
                .formatted(courseId));
        } catch (ResourceNotFoundException ignored) {
            newCourse.setId(courseId);
            return Response.success(this.courseService.save(newCourse));
        }
    }

    @DeleteMapping("/courses/{courseId}")
    public ResponseEntity<Response> deleteCourse(@PathVariable String courseId, @RequestParam("discord_id") String discordId) {
        this.courseService.deleteById(courseId, discordId);
        return Response.success();
    }
}
