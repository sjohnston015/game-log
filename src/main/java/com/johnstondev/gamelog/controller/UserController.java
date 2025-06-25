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
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserRequestDTO request) {

        // update user fields with parameters aka the updates we want to happen
        User updatedUser = userService.updateUser(id, request.getUsername(), request.getEmail());

        // make a responseDTO with new fields
        UserResponseDTO response = new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getUsername(),
                updatedUser.getEmail(),
                updatedUser.getCreatedAt()
        );

        return ResponseEntity.ok(response);
    }

    // delete a user entirely from the database
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        // try-catch block for
        try {

        } catch (RuntimeException e) {
            userService.deleteUser(id);
        }

        return ResponseEntity.noContent().build();
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
