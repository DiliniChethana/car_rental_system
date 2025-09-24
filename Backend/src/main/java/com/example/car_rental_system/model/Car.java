package com.example.car_rental_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "car")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Make cannot be blank")
    private String make;

    @NotBlank(message = "Model cannot be blank")
    private String model;

    @Min(value = 1900, message = "Year must be a valid year")
    private int year;

    @NotNull(message = "Price per day is required")
    @Min(value = 0, message = "Price per day cannot be negative")
    private double pricePerDay;

    @Column(nullable = false)
    private boolean available;
}
