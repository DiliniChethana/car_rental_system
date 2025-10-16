package com.example.car_rental_system.controller;

import com.example.car_rental_system.model.Car;
import com.example.car_rental_system.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    @Autowired
    private CarService carService;

    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }
    
    @GetMapping("/search")
    public List<Car> searchCars(
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String carType,
            @RequestParam(required = false) String pickupDate,
            @RequestParam(required = false) String returnDate,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Integer seats,
            @RequestParam(required = false) String transmission,
            @RequestParam(required = false) String sortBy) {
        return carService.searchCars(location, carType, pickupDate, returnDate, 
                                     minPrice, maxPrice, seats, transmission, sortBy);
    }
    
    @GetMapping("/featured")
    public List<Car> getFeaturedCars() {
        return carService.getFeaturedCars();
    }
    
    @GetMapping("/available")
    public List<Car> getAvailableCars() {
        return carService.getAvailableCars();
    }

    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.saveCar(car);
    }

    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        car.setId(id); // ensure ID is set before updating
        return carService.saveCar(car);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}
