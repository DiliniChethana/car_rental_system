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
    
    @Min(value = 0, message = "Hourly rate cannot be negative")
    private double hourlyRate;

    @NotBlank(message = "Availability status is required")
    private String status;
    
    private String carType; // SUV, Sedan, Coupe, etc.
    
    private String imageUrl; // URL for car image
    
    @Min(value = 0, message = "Rating must be between 0 and 5")
    private double rating;
    
    private String location; // Available location
    
    @Column(length = 1000)
    private String description;
    
    private int seats; // Number of seats
    
    private String transmission; // Automatic, Manual
    
    @Column(length = 500)
    private String features; // Comma-separated features (GPS, AC, Bluetooth, etc.)

    public boolean isAvailable() {
        return "AVAILABLE".equalsIgnoreCase(status);
    }

    public void setAvailable(boolean available) {
        this.status = available ? "AVAILABLE" : "UNAVAILABLE";
    }
}
