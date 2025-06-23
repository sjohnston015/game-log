package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.dto.CreateUserRequestDTO;
import com.johnstondev.gamelog.dto.UserResponseDTO;
import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.service.UserService;
import jakarta.validation.Valid;
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
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody CreateUserRequestDTO request) {
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

    // return a UserResponseDTO to not expose any fields that must be private (password)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        Optional<User> user = userService.findUserById(id);

        // check that if for the given id, there exists a user
        if (user.isPresent()) {
            UserResponseDTO responseDTO = new UserResponseDTO(
                    user.get().getId(),
                    user.get().getUsername(),
                    user.get().getEmail(),
                    user.get().getCreatedAt()
            );

            // return the responseDTO
            return ResponseEntity.ok(responseDTO);

        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
