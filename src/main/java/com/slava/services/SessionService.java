package com.slava.services;

import com.slava.entities.Session;
import com.slava.entities.User;
import com.slava.repositories.ISessionRepository;
import com.slava.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionService{

    public static final long SESSION_LIFE_TIME = 1000 * 60 * 60 * 2;
    private final ISessionRepository sessionRepository;
    private final IUserRepository userRepository;

    @Autowired
    public SessionService(ISessionRepository ISessionRepository, IUserRepository userRepository) {
        this.sessionRepository = ISessionRepository;
        this.userRepository = userRepository;
    }

    public Optional<User> getUserBySessionId(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        return userRepository.getUserBySessions_Id(uuid);
    }

    @Transactional
    public UUID saveSession(User user) {
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + SESSION_LIFE_TIME);
        Session session = new Session(user, expiresAt);
        sessionRepository.save(session);
        return session.getId();
    }

    public Optional<UUID> getSessionUuid(User user) {
        return Optional.ofNullable(sessionRepository.getUuidByUserId(user.getId()));
    }
}
