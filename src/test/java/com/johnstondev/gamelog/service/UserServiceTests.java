package com.johnstondev.gamelog.service;

import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

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
        User result = userService.createUser("user", "user@email.com", "hashedPassword123");

        // should be saved and should exist
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getUsername()).isEqualTo("user");
        assertThat(result.getEmail()).isEqualTo("user@email.com");
        assertThat(result.getPasswordHash()).isNotEqualTo("password123");

        // verify repo was called
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void updateUser() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("oldusername");
        user.setEmail("old@email.com");
        user.setPasswordHash("hashedpassword");
        user.setCreatedAt(LocalDateTime.now().minusDays(1));
        user.setUpdatedAt(LocalDateTime.now().minusDays(1));

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(id, "newusername", "new@email.com");

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("newusername");
        assertThat(result.getEmail()).isEqualTo("new@email.com");
        assertThat(result.getPasswordHash()).isEqualTo("hashedpassword");

        verify(userRepository, times(1)).findById(id);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void updateUsernameOnly() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("oldusername");
        user.setEmail("old@email.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(id, "newusername", null);

        assertThat(result.getUsername()).isEqualTo("newusername");
        assertThat(result.getEmail()).isEqualTo("old@email.com");
    }

    @Test
    public void updateEmailOnly() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("oldusername");
        user.setEmail("old@email.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(id, null, "new@email.com");

        assertThat(result.getUsername()).isEqualTo("oldusername");
        assertThat(result.getEmail()).isEqualTo("new@email.com");
    }

    @Test
    public void ignoreEmptyFieldsInUpdate() {
        Long id = 1L;
        User user = new User();
        user.setId(id);
        user.setUsername("oldusername");
        user.setEmail("old@email.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = userService.updateUser(id, "   ", "   ");

        assertThat(result.getUsername()).isEqualTo("oldusername");
        assertThat(result.getEmail()).isEqualTo("old@email.com");
    }

    @Test
    public void throwExceptionWhenUserNotFoundForUpdate() {
        Long id = 999L;
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(id, "newusername", "new@email.com");
        });

        assertThat(exception.getMessage()).contains("User no found with id: 999");
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void deleteUser() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);
        doNothing().when(userRepository).deleteById(id);

        userService.deleteUser(id);

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    public void throwExceptionWhenUserNotFoundForDelete() {
        Long id = 999L;

        when(userRepository.existsById(id)).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(id);
        });

        assertThat(exception.getMessage()).contains("User does not exist with id: 999");

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, never()).deleteById(id);
    }

    @Test
    public void handleRepositoryExceptionDuringDelete() {
        Long id = 1L;

        when(userRepository.existsById(id)).thenReturn(true);
        doThrow(new RuntimeException("Database connection failed"))
                .when(userRepository).deleteById(id);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(id);
        });

        assertThat(exception.getMessage()).contains("Database connection failed");

        verify(userRepository, times(1)).existsById(id);
        verify(userRepository, times(1)).deleteById(id);
    }
}
