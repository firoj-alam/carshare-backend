package com.carshare.car.service;

import com.carshare.car.dto.CarRequest;
import com.carshare.car.dto.CarResponse;
import com.carshare.car.entity.Car;
import com.carshare.car.entity.CarStatus;
import com.carshare.car.exception.DuplicateRegistrationException;
import com.carshare.car.exception.InvalidCarStateException;
import com.carshare.car.repository.CarRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car newCar() {
        return new Car(4L, "KA05AB1234", "Honda", "City", "Petrol", 2022);
    }

    @Test
    void addCar_throwsException_whenRegistrationAlreadyExists() {
        CarRequest request = new CarRequest();
        request.setRegistrationNumber("KA05AB1234");
        request.setBrand("Honda");
        request.setModel("City");
        request.setFuelType("Petrol");
        request.setManufacturingYear(2022);

        when(carRepository.existsByRegistrationNumber("KA05AB1234")).thenReturn(true);

        assertThrows(DuplicateRegistrationException.class, () -> carService.addCar(4L, request));
        verify(carRepository, never()).save(any());
    }

    @Test
    void setAvailability_throwsException_whenCarNotApproved() {
        Car car = newCar(); // starts as PENDING
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));

        assertThrows(InvalidCarStateException.class, () -> carService.setAvailability(1L, true));
        verify(carRepository, never()).save(any());
    }

    @Test
    void setAvailability_succeeds_whenCarApproved() {
        Car car = newCar();
        car.setStatus(CarStatus.APPROVED);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        CarResponse response = carService.setAvailability(1L, true);

        assertTrue(response.isAvailable());
    }

    @Test
    void setAvailability_allowsTurningOff_evenWhenNotApproved() {
        Car car = newCar();
        car.setAvailable(true);
        when(carRepository.findById(1L)).thenReturn(Optional.of(car));
        when(carRepository.save(any(Car.class))).thenAnswer(inv -> inv.getArgument(0));

        CarResponse response = carService.setAvailability(1L, false);

        assertFalse(response.isAvailable());
    }
}