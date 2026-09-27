package com.carshare.driver.dto;

import com.carshare.driver.entity.Driver;
import com.carshare.driver.entity.VerificationStatus;

import java.time.LocalDateTime;

public class DriverResponse {

    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String drivingLicense;
    private VerificationStatus verificationStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DriverResponse() {
    }

    public static DriverResponse fromEntity(Driver driver) {
        DriverResponse response = new DriverResponse();
        response.id = driver.getId();
        response.name = driver.getName();
        response.email = driver.getEmail();
        response.mobile = driver.getMobile();
        response.drivingLicense = driver.getDrivingLicense();
        response.verificationStatus = driver.getVerificationStatus();
        response.createdAt = driver.getCreatedAt();
        response.updatedAt = driver.getUpdatedAt();
        return response;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getDrivingLicense() {
        return drivingLicense;
    }

    public VerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}