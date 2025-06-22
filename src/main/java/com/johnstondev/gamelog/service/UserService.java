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
}
