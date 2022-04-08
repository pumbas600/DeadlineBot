package net.pumbas.deadlinebot.repositories.user;

import net.pumbas.deadlinebot.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String>
{
}
