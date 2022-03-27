package com.cabservice.lld.exception;

public class TripNotFoundException extends RuntimeException {

    public TripNotFoundException(String message) {
        super(message);
    }
}
