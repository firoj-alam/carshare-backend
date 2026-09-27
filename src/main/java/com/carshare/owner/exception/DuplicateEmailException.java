package com.carshare.owner.exception;

public class DuplicateEmailException extends RuntimeException {

    public DuplicateEmailException(String email) {
        super("An owner with email '" + email + "' already exists.");
    }
}