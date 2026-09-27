package com.carshare.car.exception;

public class DuplicateRegistrationException extends RuntimeException {
    public DuplicateRegistrationException(String registrationNumber) {
        super("A car with registration number '" + registrationNumber + "' already exists.");
    }
}