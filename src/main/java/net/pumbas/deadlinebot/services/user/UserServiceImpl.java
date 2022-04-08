package net.pumbas.deadlinebot.services.user;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.User;

public class UserServiceImpl implements UserService
{
    @Override
    public User findById(String userId) throws ResourceNotFoundException {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void deleteById(String userId) throws ResourceNotFoundException {

    }

    @Override
    public void delete(User user) {

    }
}
