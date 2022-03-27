package com.cabservice.lld.exception;

public class CabAlreadyRegisteredException extends RuntimeException {

    public CabAlreadyRegisteredException(String message) {
        super(message);
    }
}
