package com.carshare.owner.exception;

public class OwnerNotFoundException extends RuntimeException {

    public OwnerNotFoundException(Long id) {
        super("Owner not found with id: " + id);
    }
}