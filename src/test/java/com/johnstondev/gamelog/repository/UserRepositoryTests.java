package com.johnstondev.gamelog.repository;

import com.johnstondev.gamelog.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

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
        User user = new User(null, "user", "test@email.com");
        User saved = userRepository.save(user);

        // check if user was saved
        assertThat(saved.getId()).isNotNull(); // ID should be made automatically
        assertThat(saved.getUsername()).isEqualTo("user");
        assertThat(saved.getEmail()).isEqualTo("test@email.com");

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
}
