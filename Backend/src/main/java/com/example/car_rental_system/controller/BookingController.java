package com.example.car_rental_system.controller;

import com.example.car_rental_system.model.Booking;
import com.example.car_rental_system.model.User;
import com.example.car_rental_system.repository.UserRepository;
import com.example.car_rental_system.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
@CrossOrigin(origins = "http://localhost:3000")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<?> createBooking(@Valid @RequestBody Booking booking) {
        try {
            System.out.println("=== Booking Request Received ===");
            System.out.println("Booking data: " + booking);
            System.out.println("Car: " + (booking.getCar() != null ? booking.getCar().getId() : "null"));
            System.out.println("Pickup Date: " + booking.getPickupDate());
            System.out.println("Pickup Location: " + booking.getPickupLocation());
            System.out.println("Pickup Time: " + booking.getPickupTime());
            
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("Authentication: " + authentication);
            System.out.println("Auth name: " + authentication.getName());
            
            String currentUsername = authentication.getName();
            User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found: " + currentUsername));
            
            System.out.println("Current user: " + currentUser.getUsername());
            
            booking.setUser(currentUser);
            Booking savedBooking = bookingService.createBooking(booking);
            
            System.out.println("Booking saved successfully: " + savedBooking.getId());
            return ResponseEntity.ok(savedBooking);
        } catch (Exception e) {
            System.err.println("Error creating booking: " + e.getMessage());
            e.printStackTrace();
            
            // Return detailed error message
            String errorMsg = e.getMessage();
            if (e.getCause() != null) {
                errorMsg += " - Cause: " + e.getCause().getMessage();
            }
            return ResponseEntity.badRequest().body("{\"message\": \"" + errorMsg + "\"}");
        }
    }

    @GetMapping
    public List<Booking> getAllBookings() {
        return bookingService.getAllBookings();
    }

    @GetMapping("/user/{userId}")
    public List<Booking> getBookingsByUserId(@PathVariable Long userId) {
        return bookingService.getBookingsByUserId(userId);
    }

    @PutMapping("/{id}/approve")
    public Booking approveBooking(@PathVariable Long id) {
        return bookingService.approveBooking(id);
    }

    @PutMapping("/{id}/cancel")
    public Booking cancelBooking(@PathVariable Long id) {
        return bookingService.cancelBooking(id);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            System.err.println("Validation error - " + fieldName + ": " + errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}