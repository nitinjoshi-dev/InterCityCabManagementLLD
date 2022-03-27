package com.cabservice.lld.exception;

public class NoAvailableCabsException extends RuntimeException {
    public NoAvailableCabsException(String message) {
        super(message);
    }
}
