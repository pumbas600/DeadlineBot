package net.pumbas.deadlinebot.services.user;

import net.pumbas.deadlinebot.exceptions.ResourceNotFoundException;
import net.pumbas.deadlinebot.models.User;
import net.pumbas.deadlinebot.repositories.user.UserRepository;

public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(String userId) throws ResourceNotFoundException {
        return this.userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("There is no user with the id " + userId));
    }

    @Override
    public User save(User user) {
        return this.userRepository.save(user);
    }

    @Override
    public void deleteById(String userId) throws ResourceNotFoundException {
        if (this.userRepository.existsById(userId))
            this.userRepository.deleteById(userId);
        else throw new ResourceNotFoundException("There is no user with the id " + userId);
    }
}
