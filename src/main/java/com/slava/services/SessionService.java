package com.slava.services;

import com.slava.entities.Session;
import com.slava.entities.User;
import com.slava.repositories.ISessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.UUID;

@Service
public class SessionService{

    public static final long SESSION_LIFE_TIME = 1000 * 60 * 60 * 2;
    ISessionRepository sessionRepository;

    @Autowired
    public SessionService(ISessionRepository ISessionRepository) {
        this.sessionRepository = ISessionRepository;
    }

    public User getUserBySession(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        Session session = sessionRepository.findById(uuid).orElse(null);
        User user = session.getUser();
        return user;
    }

    @Transactional
    public UUID saveSession(User user) {
        Timestamp expiresAt = new Timestamp(System.currentTimeMillis() + SESSION_LIFE_TIME);
        Session session = new Session(user, expiresAt);
        sessionRepository.save(session);
        return session.getId();
    }
}
