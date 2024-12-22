package com.slava.repositories;

import com.slava.entities.Location;
import com.slava.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    Optional<User> getUserBySessions_Id(UUID sessionId);
}
