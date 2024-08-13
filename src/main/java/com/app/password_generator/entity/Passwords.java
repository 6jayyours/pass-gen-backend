package com.app.password_generator.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "passwords")
@Data
public class Passwords {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String password;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
