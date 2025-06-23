package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.dto.CreateUserRequestDTO;
import com.johnstondev.gamelog.dto.UserResponseDTO;
import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody CreateUserRequestDTO request) {
        User createdUser = userService.createUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword()
        );

        UserResponseDTO response = new UserResponseDTO(
                createdUser.getId(),
                createdUser.getUsername(),
                createdUser.getEmail(),
                createdUser.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public Optional<User> findUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }
}
