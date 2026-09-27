package com.carshare.driver.exception;

public class DuplicateLicenseException extends RuntimeException {

    public DuplicateLicenseException(String drivingLicense) {
        super("A driver with driving license '" + drivingLicense + "' already exists.");
    }
}