package com.johnstondev.gamelog.controller;

import com.johnstondev.gamelog.dto.CreateUserRequestDTO;
import com.johnstondev.gamelog.dto.UpdateUserRequestDTO;
import com.johnstondev.gamelog.dto.UserResponseDTO;
import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    // POST -> Create // GET -> Read // PUT -> Update // DELETE -> Delete

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

    // update user fields, such as a user's username or email address
    // need to move away from the try-catch blocks and figure out a better way for error-handling
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO request) {
        try {
            // update user fields with the parameters aka the updates we want to happen
            User updatedUser = userService.updateUser(id, request.getUsername(), request.getEmail());

            UserResponseDTO response = new UserResponseDTO(
                    updatedUser.getId(),
                    updatedUser.getUsername(),
                    updatedUser.getEmail(),
                    updatedUser.getCreatedAt()
            );

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // delete a user entirely from the database
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // try-catch block for exception-handling in UserController
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // get all users -> PLANNING to use pageables later
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<User> allUsers = userService.getAllUsers();
        List<UserResponseDTO> allUsersDTO = new ArrayList<>();

        for (User user : allUsers) {
            UserResponseDTO responseDTO = new UserResponseDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getCreatedAt()
            );

            allUsersDTO.add(responseDTO);
        }

        return ResponseEntity.ok(allUsersDTO);
    }
}
