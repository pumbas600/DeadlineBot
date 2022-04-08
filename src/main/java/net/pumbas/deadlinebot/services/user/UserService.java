package net.pumbas.deadlinebot.services.user;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.User;

public interface UserService
{
    User findById(String userId) throws ResourceNotFoundException;

    User save(User user);

    default User update(User user) {
        return this.save(user);
    }

    void deleteById(String userId) throws ResourceNotFoundException;

    void delete(User user);
}
