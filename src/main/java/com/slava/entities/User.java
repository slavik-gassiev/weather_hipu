package com.slava.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@ToString(exclude = {"sessions", "locations"})
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Логин не может быть пустым или содержать только пробелы")
    @Size(min = 3, max = 50, message = "Логин должен быть от 3 до 50 символов")
    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @NotBlank(message = "Пароль не может быть пустым или содержать только пробелы")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Location> locations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Session> sessions;
}
