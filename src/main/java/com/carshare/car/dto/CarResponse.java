package com.carshare.car.dto;

import com.carshare.car.entity.Car;
import com.carshare.car.entity.CarStatus;

import java.time.LocalDateTime;

public class CarResponse {

    private Long id;
    private Long ownerId;
    private String registrationNumber;
    private String brand;
    private String model;
    private String fuelType;
    private Integer manufacturingYear;
    private boolean available;
    private CarStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CarResponse() {
    }

    public static CarResponse fromEntity(Car car) {
        CarResponse response = new CarResponse();
        response.id = car.getId();
        response.ownerId = car.getOwnerId();
        response.registrationNumber = car.getRegistrationNumber();
        response.brand = car.getBrand();
        response.model = car.getModel();
        response.fuelType = car.getFuelType();
        response.manufacturingYear = car.getManufacturingYear();
        response.available = car.isAvailable();
        response.status = car.getStatus();
        response.createdAt = car.getCreatedAt();
        response.updatedAt = car.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public String getFuelType() {
        return fuelType;
    }

    public Integer getManufacturingYear() {
        return manufacturingYear;
    }

    public boolean isAvailable() {
        return available;
    }

    public CarStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}