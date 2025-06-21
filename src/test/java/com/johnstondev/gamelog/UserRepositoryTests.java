package com.johnstondev.gamelog;

import com.johnstondev.gamelog.model.User;
import com.johnstondev.gamelog.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles("test") // please work...
public class UserRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveAndFindUser() {

        // create user, then save user
        User user = new User(1L, "user", "test@email.com");
        User saved = userRepository.save(user);

        // check if user was saved
        assertEquals(1L, saved.getId());
        assertEquals("user", saved.getUsername());
        assertEquals("test@email.com", saved.getEmail());

        // check if user was actually saved and can be found
        Optional<User> found = userRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("user", found.get().getUsername());
    }

}
