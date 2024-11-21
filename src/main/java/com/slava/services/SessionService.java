package com.slava.services;

import com.slava.entities.Session;
import com.slava.entities.User;
import com.slava.repositories.SessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService{

    SessionRepository sessionRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public User getUserBySession(String sessionId) {
        UUID uuid = UUID.fromString(sessionId);
        Session session = sessionRepository.findById(uuid).orElse(null);
        User user = session.getUser();
        return user;
    }
}
