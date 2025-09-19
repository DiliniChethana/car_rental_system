package com.example.car_rental_system.repository;// src/main/java/com/example/carrentalsystem/repository/UserRepository.java
import com.example.car_rental_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}