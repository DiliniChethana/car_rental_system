package com.example.car_rental_system.model;// src/main/java/com/example/carrentalsystem/model/Booking.java
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.temporal.Temporal;

@Data
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDate startDate;
    private LocalDate endDate;
    private String status;
    private double totalPrice;


    public Car getCar() {
        return null;
    }

    public Car getUser() {
        return null;
    }

    public void setCar(Car car) {
    }

    public void setUser(User user) {
    }

    public Temporal getStartDate() {
        return null;
    }

    public Temporal getEndDate() {
        return null;
    }

    public void setTotalPrice(double v) {
    }

    public void setStatus(String pending) {
    }

    public Object getStatus() {
        return null;
    }
}