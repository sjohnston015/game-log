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

    // TODO: Implement better password hashing LOL
    public User createUser(String username, String email, String password) {
        String passwordHash = password + "-hashed";
        User createdUser = new User(null, username, email, passwordHash);
        return userRepository.save(createdUser);
    }

    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // TODO: Allow users to update their password, safely and securely
    public User updateUser(Long id, String username, String email) {

        // find user; throw RuntimeException if a User is not found with given id
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            throw new RuntimeException("User no found with id: " + id);
        }

        // update username/email fields
        User user = userOptional.get();

        // verify parameters are valid and update corresponding fields
        if ((username != null && !username.trim().isEmpty())) {
            user.setUsername(username);
        }

        if ((email != null && !email.trim().isEmpty())) {
            user.setEmail(email);
        }

        return userRepository.save(user);
    }
}
