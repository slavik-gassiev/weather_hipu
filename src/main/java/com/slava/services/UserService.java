package com.slava.services;

import com.slava.entities.User;
import com.slava.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {
    private final IUserRepository userRepository;

    @Autowired
    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    public Optional<User> findByLoginAndPassword(String login, String password) {
        Optional<User> user = findByLogin(login);
        return user.filter(u -> BCrypt.checkpw(password, u.getPassword()));
    }

    @Transactional
    public Long saveUser(User user) {
        String securedPassword = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt(12));
        user.setPassword(securedPassword);
        return userRepository.save(user).getId();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User saveAndGetUser(User user) {
        Long id = saveUser(user);
        return findById(id).get();
    }
}
