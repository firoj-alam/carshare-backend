package com.carshare.car.service;

import com.carshare.car.dto.CarRequest;
import com.carshare.car.dto.CarResponse;
import com.carshare.car.entity.Car;
import com.carshare.car.entity.CarStatus;
import com.carshare.car.exception.CarNotFoundException;
import com.carshare.car.exception.DuplicateRegistrationException;
import com.carshare.car.exception.InvalidCarStateException;
import com.carshare.car.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }
    public CarResponse verifyCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.setStatus(CarStatus.APPROVED);
        return CarResponse.fromEntity(carRepository.save(car));
    }

    public CarResponse rejectCar(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        car.setStatus(CarStatus.REJECTED);
        return CarResponse.fromEntity(carRepository.save(car));
    }

    public CarResponse addCar(Long ownerId, CarRequest request) {
        if (carRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new DuplicateRegistrationException(request.getRegistrationNumber());
        }

        Car car = new Car(
                ownerId,
                request.getRegistrationNumber(),
                request.getBrand(),
                request.getModel(),
                request.getFuelType(),
                request.getManufacturingYear()
        );

        Car savedCar = carRepository.save(car);
        return CarResponse.fromEntity(savedCar);
    }

    public CarResponse getCarById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return CarResponse.fromEntity(car);
    }

    public List<CarResponse> getAllCars() {
        return carRepository.findAll()
                .stream()
                .map(CarResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getCarsByOwner(Long ownerId) {
        return carRepository.findByOwnerId(ownerId)
                .stream()
                .map(CarResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public List<CarResponse> getAvailableCars() {
        return carRepository.findByStatusAndAvailable(CarStatus.APPROVED, true)
                .stream()
                .map(CarResponse::fromEntity)
                .collect(Collectors.toList());
    }

    public CarResponse updateCar(Long id, CarRequest request) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        car.setBrand(request.getBrand());
        car.setModel(request.getModel());
        car.setFuelType(request.getFuelType());
        car.setManufacturingYear(request.getManufacturingYear());
        // registrationNumber intentionally not updatable here — changing a
        // vehicle's registration number is a sensitive, rare operation that
        // deserves its own explicit flow, not a generic update endpoint.

        Car updatedCar = carRepository.save(car);
        return CarResponse.fromEntity(updatedCar);
    }

    public CarResponse setAvailability(Long id, boolean available) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        if (available && car.getStatus() != CarStatus.APPROVED) {
            throw new InvalidCarStateException(
                    "Car must be APPROVED before it can be made available. Current status: " + car.getStatus());
        }

        car.setAvailable(available);
        Car updatedCar = carRepository.save(car);
        return CarResponse.fromEntity(updatedCar);
    }

    public void deleteCar(Long id) {
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException(id);
        }
        carRepository.deleteById(id);
    }
}