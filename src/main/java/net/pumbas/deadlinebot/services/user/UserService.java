package net.pumbas.deadlinebot.services.user;

import net.pumbas.deadlinebot.exceptions.BadRequestException;
import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.PostUser;
import net.pumbas.deadlinebot.models.User;

import java.util.List;

public interface UserService
{
    User findById(String userId) throws ResourceNotFoundException;

    User save(User user);

    User save(PostUser user) throws BadRequestException;

    void deleteById(String userId) throws ResourceNotFoundException;

    User updateCourses(String userId, List<String> courseId) throws ResourceNotFoundException, BadRequestException;
}
