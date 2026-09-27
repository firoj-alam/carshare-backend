package com.carshare.car.repository;

import com.carshare.car.entity.Car;
import com.carshare.car.entity.CarStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {

    List<Car> findByOwnerId(Long ownerId);

    List<Car> findByStatusAndAvailable(CarStatus status, boolean available);

    boolean existsByRegistrationNumber(String registrationNumber);
}