package com.carshare.car.controller;

import com.carshare.car.dto.CarRequest;
import com.carshare.car.dto.CarResponse;
import com.carshare.car.service.CarService;
import com.carshare.security.SecurityUtil;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping
    public ResponseEntity<CarResponse> addCar(@Valid @RequestBody CarRequest request) {
        Long ownerId = SecurityUtil.getCurrentUserId();
        CarResponse response = carService.addCar(ownerId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponse> getCarById(@PathVariable Long id) {
        CarResponse response = carService.getCarById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<CarResponse>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<CarResponse>> getCarsByOwner(@PathVariable Long ownerId) {
        return ResponseEntity.ok(carService.getCarsByOwner(ownerId));
    }

    @GetMapping("/available")
    public ResponseEntity<List<CarResponse>> getAvailableCars() {
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarResponse> updateCar(
            @PathVariable Long id,
            @Valid @RequestBody CarRequest request) {
        CarResponse response = carService.updateCar(id, request);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/availability")
    public ResponseEntity<CarResponse> setAvailability(
            @PathVariable Long id,
            @RequestParam boolean available) {
        CarResponse response = carService.setAvailability(id, available);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }
}