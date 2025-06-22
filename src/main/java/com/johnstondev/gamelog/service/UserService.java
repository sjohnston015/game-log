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

    public User createUser(String username, String email, String passwordHash) {
        User createdUser = new User(null, username, email, passwordHash);
        return userRepository.save(createdUser);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }
}
