package com.example.car_rental_system.service;

import com.example.car_rental_system.model.Car;
import com.example.car_rental_system.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Car getCarById(Long id) {
        return carRepository.findById(id).orElse(null);
    }
    
    public List<Car> searchCars(String location, String carType, String pickupDate, String returnDate,
                                 Double minPrice, Double maxPrice, Integer seats, 
                                 String transmission, String sortBy) {
        List<Car> cars = carRepository.findAll();
        
        List<Car> filteredCars = cars.stream()
                .filter(car -> car.isAvailable())
                .filter(car -> location == null || location.isEmpty() || 
                        (car.getLocation() != null && car.getLocation().toLowerCase().contains(location.toLowerCase())))
                .filter(car -> carType == null || carType.isEmpty() || 
                        (car.getCarType() != null && car.getCarType().equalsIgnoreCase(carType)))
                .filter(car -> minPrice == null || car.getPricePerDay() >= minPrice)
                .filter(car -> maxPrice == null || car.getPricePerDay() <= maxPrice)
                .filter(car -> seats == null || car.getSeats() >= seats)
                .filter(car -> transmission == null || transmission.isEmpty() ||
                        (car.getTransmission() != null && car.getTransmission().equalsIgnoreCase(transmission)))
                .collect(Collectors.toList());
        
        // Apply sorting
        if (sortBy != null && !sortBy.isEmpty()) {
            switch (sortBy.toLowerCase()) {
                case "price_low":
                    filteredCars.sort(Comparator.comparingDouble(Car::getPricePerDay));
                    break;
                case "price_high":
                    filteredCars.sort(Comparator.comparingDouble(Car::getPricePerDay).reversed());
                    break;
                case "rating":
                    filteredCars.sort(Comparator.comparingDouble(Car::getRating).reversed());
                    break;
                default:
                    break;
            }
        }
        
        return filteredCars;
    }
    
    public List<Car> getFeaturedCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .filter(car -> car.isAvailable())
                .filter(car -> car.getRating() >= 4.0)
                .limit(6)
                .collect(Collectors.toList());
    }
    
    public List<Car> getAvailableCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());
    }

    public Car saveCar(Car car) {
        return carRepository.save(car);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}