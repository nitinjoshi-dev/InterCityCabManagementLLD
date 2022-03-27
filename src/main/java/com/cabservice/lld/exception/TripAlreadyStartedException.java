package com.cabservice.lld.exception;

public class TripAlreadyStartedException extends RuntimeException {

    public TripAlreadyStartedException(String message) {
        super(message);
    }
}
