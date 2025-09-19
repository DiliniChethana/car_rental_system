package com.example.car_rental_system.model;// src/main/java/com/example/carrentalsystem/model/User.java
import jakarta.persistence.*;
import lombok.Data; // Consider using Lombok for less boilerplate code

@Data // Lombok annotation for getters, setters, toString, etc.
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String role; // e.g., "ROLE_USER", "ROLE_ADMIN"
}