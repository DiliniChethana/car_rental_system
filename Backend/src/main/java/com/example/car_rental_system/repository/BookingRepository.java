package com.example.car_rental_system.repository;// src/main/java/com/example/carrentalsystem/repository/BookingRepository.java
import com.example.car_rental_system.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByUserId(Long userId); // Custom method to get a user's bookings
}