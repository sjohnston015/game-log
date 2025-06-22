package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    public void createUser() {

        // set all User fields
        User saved = new User();
        saved.setId(1L);
        saved.setUsername("user");
        saved.setEmail("user@email.com");
        saved.setPasswordHash("hashedPassword123");

        // mock repo to return saved user
        when(userRepository.save(any(User.class))).thenReturn(saved);

        // UserService method that doesn't exist yet
        User result = userService.createUser(1L, "user", "user@email.com");

        // should be saved and should exist
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("user");
        assertThat(result.getEmail()).isEqualTo("user@email.com");
        assertThat(result.getPasswordHash()).isNotEqualTo("password123"); // Should be hashed

        // verify repo was called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void findUserById() {

        Long userId = 1L;
        User user = new User();
        user.setId(userId);
        user.setUsername("user");
        user.setEmail("user@email.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findUserById(userId);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(userId);
        assertThat(result.get().getUsername()).isEqualTo("user");

        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    public void returnEmptyWhenUserNotFound() {

        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Optional<User> result = userService.findUserById(userId);

        assertThat(result).isEmpty();
        verify(userRepository, times(1)).findById(userId);
    }
}
