package com.slava.repositories;

import com.slava.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISessionRepository extends JpaRepository<Session, UUID> {
    Session getByUser_Id(Long id);

    @Query("SELECT s.id FROM Session s WHERE s.user.id = :userId")
    UUID getUuidByUserId(@Param("userId") Long userId);
}
