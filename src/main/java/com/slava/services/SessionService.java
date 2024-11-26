package com.slava.services;

import com.slava.entities.Session;
import com.slava.entities.User;
import com.slava.repositories.ISessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService{

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
}
