package com.carshare.driver.exception;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(Long id) {
        super("Driver not found with id: " + id);
    }
}