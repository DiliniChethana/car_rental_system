package com.example.car_rental_system.service;

import com.example.car_rental_system.model.Booking;
import com.example.car_rental_system.model.Car;
import com.example.car_rental_system.repository.BookingRepository;
import com.example.car_rental_system.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    public Booking createBooking(Booking booking) {
        Car car = carRepository.findById(booking.getCar().getId())
            .orElseThrow(() -> new RuntimeException("Car not found"));
        
        // User is set by the controller, so we just need to validate it exists
        if (booking.getUser() == null) {
            throw new RuntimeException("User not found");
        }

        booking.setCar(car);
        
        // Set default status if not provided
        if (booking.getStatus() == null || booking.getStatus().isEmpty()) {
            booking.setStatus("PENDING");
        }

        return bookingRepository.save(booking);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingRepository.findByUserId(userId);
    }

    public Booking approveBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            booking.setStatus("CONFIRMED");
            return bookingRepository.save(booking);
        }
        return null;
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null && (booking.getStatus().equals("PENDING") || booking.getStatus().equals("CONFIRMED"))) {
            booking.setStatus("CANCELED");
            booking.getCar().setAvailable(true); // Make the car available again
            carRepository.save(booking.getCar());
            return bookingRepository.save(booking);
        }
        return null;
    }
}