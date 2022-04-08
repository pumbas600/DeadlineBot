package net.pumbas.deadlinebot.controllers;

import net.pumbas.deadlinebot.App;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.exceptions.UnauthorizedAccessException;
import net.pumbas.deadlinebot.models.Course;
import net.pumbas.deadlinebot.services.course.CourseService;

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
public class CourseController
{
    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/courses/tracked")
    public List<Course> getCoursesTrackedBy(@RequestParam("discordId") String discordId) {
        return this.courseService.findAllTrackedBy(discordId);
    }

    @GetMapping("/courses/owned")
    public List<Course> getCoursesOwnedBy(@RequestParam("discordId") String discordId) {
        return this.courseService.findAllOwnedBy(discordId);
    }

    @GetMapping("/courses/{courseId}")
    public Course getCourse(@PathVariable String courseId, @RequestParam("discord_id") String discordId) {
        Course course = this.courseService.findById(courseId);
        if (course.isPublic() || course.getOwnerId().equals(discordId))
            return course;
        throw new UnauthorizedAccessException("There's no course with the id " + courseId);
    }

    @PostMapping("/courses")
    public Course newCourse(@RequestBody Course course, @RequestParam("discord_id") String discordId) {
        course.setOwnerId(discordId);
        course.setId(null); // Automatically generate the course id
        return this.courseService.save(course);
    }

    @PutMapping("/courses/{courseId}")
    public Course replaceCourse(@PathVariable String courseId,
                                @RequestBody Course newCourse,
                                @RequestParam("discord_id") String discordId
    ) {
        newCourse.setOwnerId(discordId);
        try {
            Course course = this.courseService.findById(courseId);
            if (course.isPublic() || course.getOwnerId().equals(discordId)) {
                newCourse.setId(course.getId());
                return this.courseService.save(newCourse);
            }
            else throw new UnauthorizedAccessException("You aren't the owner of the course %s so you cannot update it"
                .formatted(courseId));
        } catch (ResourceNotFoundException ignored) {
            newCourse.setId(courseId);
            return this.courseService.save(newCourse);
        }
    }

    @DeleteMapping("/courses/{courseId}")
    public void deleteCourse(@PathVariable String courseId, @RequestParam("discord_id") String discordId) {
        this.courseService.deleteById(courseId, discordId);
    }
}
