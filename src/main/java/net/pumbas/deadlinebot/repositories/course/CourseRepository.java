package net.pumbas.deadlinebot.repositories.course;

import net.pumbas.deadlinebot.models.Course;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface CourseRepository extends MongoRepository<Course, String>
{
    @Query("{owner_id:'?0'}")
    List<Course> findCoursesOwnedBy(String discordId);


}
