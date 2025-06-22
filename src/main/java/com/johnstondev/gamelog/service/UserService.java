package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(Long id, String username, String email) {
        User createdUser = new User(id, username, email);
        userRepository.save(createdUser);
        return createdUser;
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
