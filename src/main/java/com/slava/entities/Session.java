package com.slava.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Timestamp;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "sessions")
public class Session {

    @Id
    @Column(name = "id")
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    @Column(name = "expire_at")
    private Timestamp expireAt;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;
}
