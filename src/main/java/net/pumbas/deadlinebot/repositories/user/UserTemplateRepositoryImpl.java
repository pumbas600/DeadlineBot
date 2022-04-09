package net.pumbas.deadlinebot.repositories.user;

import net.pumbas.deadlinebot.models.User;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class UserTemplateRepositoryImpl implements UserTemplateRepository
{
    private final MongoTemplate mongoTemplate;

    public UserTemplateRepositoryImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void deletePublicCourseReferences(String discordId, String courseId) {
        Query query = Query.query(Criteria.where("_id").ne(discordId));
        Update update = new Update().pull("courses", new ObjectId(courseId));
        this.mongoTemplate.updateMulti(query, update, User.class);
    }

    @Override
    public void deleteAllCourseReferences(String courseId) {
        Update update = new Update().pull("courses", new ObjectId(courseId));
        this.mongoTemplate.updateMulti(new Query(), update, User.class);
    }
}
