package com.slava.services;

import com.slava.config.TestConfig;
import com.slava.entities.Session;
import com.slava.entities.User;
import com.slava.repositories.ISessionRepository;
import com.slava.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(TestConfig.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class SessionServiceIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ISessionRepository sessionRepository;

    @BeforeEach
    void setUp() {
        // Очищаем базу данных перед каждым тестом
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void createSession_createsNewSessionForUser() {
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("password");
        userRepository.save(user);

        UUID uuid = sessionService.saveSession(user);
        Optional<Session> session = sessionService.getSession(uuid);
        assertNotNull(session.isPresent());

        Optional<Session> savedSession = sessionRepository.findById(session.get().getId());
        assertTrue(savedSession.isPresent());
        assertEquals(user.getId(), savedSession.get().getUser().getId());
    }

    @Test
    void expiredSession_isNotValid() {
        // Тест логики истечения сессии
        User user = new User();
        user.setLogin("testUser");
        user.setPassword("password");
        userRepository.save(user);

        UUID uuid = sessionService.saveSession(user);
        Optional<Session> session = sessionService.getSession(uuid);

        Timestamp expiredTimestamp = new Timestamp(System.currentTimeMillis() - 1000);
        session.get().setExpireAt(expiredTimestamp);
        sessionRepository.save(session.get());

        assertTrue(sessionService.isSessionExpired(session.get()));
    }
}
