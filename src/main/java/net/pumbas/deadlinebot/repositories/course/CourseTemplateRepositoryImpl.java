package net.pumbas.deadlinebot.repositories.course;

import com.mongodb.client.result.DeleteResult;

import net.pumbas.deadlinebot.models.Course;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

@Component
public class CourseTemplateRepositoryImpl implements CourseTemplateRepository
{
    private final MongoTemplate template;

    public CourseTemplateRepositoryImpl(MongoTemplate template) {
        this.template = template;
    }

    @Override
    public boolean deleteCourseWithIdAndOwnedBy(String courseId, String discordId) {
        Query query = Query.query(Criteria.where("id").is(courseId).and("owner_id").is(discordId));
        DeleteResult result = this.template.remove(query, Course.class);
        return result.getDeletedCount() != 0;
    }
}
