package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    // constructor injection
    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(String username, String email, String password) {
        String passwordHash = password + "-hashed";
        User createdUser = new User(null, username, email, passwordHash);
        return userRepository.save(createdUser);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public User updateUser(Long id, String username, String email) {

        // find user; throw RuntimeException if a user is not found with given id
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User no found with id: " + id);
        }

        // update username/email fields
        // TODO: Allow users to update their password, safely and securely
        // password updates will be handled differently for security

        User user = userOptional.get();

        if ((username != null && !username.trim().isEmpty())) {
            user.setUsername(username);
        }

        if ((email != null && !email.trim().isEmpty())) {
            user.setEmail(email);
        }

        return userRepository.save(user);
    }
}
