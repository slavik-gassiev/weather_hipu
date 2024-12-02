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
    private IUserRepository userRepository;

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
    public Long saveUser(String login, String password) {
        return userRepository.saveUser(login, password);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public User saveAndGetUser(String login, String password) {
        String securedPassword = BCrypt.hashpw(password, BCrypt.gensalt(12));
        Long id = saveUser(login, securedPassword);
        return findById(id).get();
    }

}
