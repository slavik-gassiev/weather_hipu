package com.slava.services;

import com.slava.config.TestConfig;
import com.slava.config.WebConfig;
import com.slava.entities.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(TestConfig.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class UserServiceIntegrationTest {

    @Autowired
    private UserService userService;

    @Test
    void registerUser_createsNewUserInDatabase() {
        User user = new User();
        user.setLogin("newUser");
        user.setPassword("securePassword12345678!");

        Optional<User> savedUser = Optional.ofNullable(userService.saveAndGetUser(user));
        assertTrue(savedUser.isPresent());
        assertEquals("newUser", savedUser.get().getLogin());
    }

    @Test
    void registerUser_withDuplicateLogin_throwsException() {
        User user1 = new User();
        user1.setLogin("duplicateUser");
        user1.setPassword("password");
        userService.saveUser(user1);

        User user2 = new User();
        user2.setLogin("duplicateUser");
        user2.setPassword("anotherPassword");

        assertThrows(RuntimeException.class, () -> userService.saveUser(user2));
    }
}
