package com.carshare.driver.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("A driver with email '" + email + "' already exists.");
    }
}