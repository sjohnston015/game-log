package com.johnstondev.gamelog.repository;

import com.johnstondev.gamelog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndFindUser() {

        // create user, then save user
        LocalDateTime now = LocalDateTime.now();
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@email.com");
        user.setPasswordHash("testPassword123");
        user.setCreatedAt(now);
        user.setUpdatedAt(now);

        User saved = userRepository.save(user);

        // check if user was saved
        assertThat(saved.getId()).isNotNull(); // ID should be made automatically
        assertThat(saved.getUsername()).isEqualTo("user");
        assertThat(saved.getEmail()).isEqualTo("user@email.com");
        assertThat(saved.getPasswordHash()).isEqualTo("testPassword123");

        // check if user can be found
        Optional<User> found = userRepository.findById(saved.getId());
        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("user");
    }

    @Test
    public void shouldReturnEmptyWhenUserNotFound() {

        // should return empty for a user that does not exist
        Optional<User> found = userRepository.findById(999L);
        assertThat(found).isEmpty();
    }

    @Test
    public void saveAllUserFields() {

        // set all user fields, @PrePersist annotation (in User class) for timestamps
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@email.com");
        user.setPasswordHash("hashedPassword123");

        // save that
        User saved = userRepository.save(user);

        // verify they saved properly
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUsername()).isEqualTo("user");
        assertThat(saved.getEmail()).isEqualTo("user@email.com");
        assertThat(saved.getPasswordHash()).isEqualTo("hashedPassword123");
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    public void shouldSetTimestampsAutomatically() {

        // not setting createdAt or updatedAt fields
        User user = new User();
        user.setUsername("user");
        user.setEmail("user@email.com");
        user.setPasswordHash("password123");

        User saved = userRepository.save(user);
        LocalDateTime save = LocalDateTime.now();

        // timestamps should be set automatically...
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(saved.getCreatedAt()).isEqualTo(saved.getUpdatedAt());

        // they should be recent as well
        assertThat(saved.getCreatedAt()).isBefore(save.plusSeconds(1));
        assertThat(saved.getCreatedAt()).isAfter(save.minusSeconds(5));
    }
}
