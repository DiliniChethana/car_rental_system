package com.example.car_rental_system.repository;

import com.example.car_rental_system.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, Long> {
}