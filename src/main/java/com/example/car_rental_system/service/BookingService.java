package com.example.car_rental_system.service;

import com.example.car_rental_system.model.Booking;
import com.example.car_rental_system.model.Car;
import com.example.car_rental_system.model.User;
import com.example.car_rental_system.repository.BookingRepository;
import com.example.car_rental_system.repository.CarRepository;
import com.example.car_rental_system.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private UserRepository userRepository;

    public Booking createBooking(Booking booking) {
        // Find the car and user by their IDs
        Car car = carRepository.findById(booking.getCar().getId()).orElse(null);
        User user = userRepository.findById(booking.getUser().getId()).orElse(null);

        // Check if car and user exist and if the car is available
        if (car == null || user == null || !car.isAvailable()) {
            // In a real application, you would throw a specific exception here
            return null;
        }

        // Set the car and user objects on the booking
        booking.setCar(car);
        booking.setUser(user);

        // Calculate total price based on the number of days
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate());
        booking.setTotalPrice(days * car.getPricePerDay());
        booking.setStatus("PENDING");

        // Make the car unavailable and save it
        car.setAvailable(false);
        carRepository.save(car);

        // Save the booking
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