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

    // GET all cars
    @GetMapping
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    // GET a single car by ID
    @GetMapping("/{id}")
    public Car getCarById(@PathVariable Long id) {
        return carService.getCarById(id);
    }

    // POST a new car (create)
    @PostMapping
    public Car createCar(@RequestBody Car car) {
        return carService.saveCar(car);
    }

    // PUT to update an existing car
    @PutMapping("/{id}")
    public Car updateCar(@PathVariable Long id, @RequestBody Car car) {
        // Set the ID from the URL path to ensure the correct car is updated
        car.setId(id);
        return carService.saveCar(car);
    }

    // DELETE a car by ID
    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
    }
}