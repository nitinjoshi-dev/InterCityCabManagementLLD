package com.cabservice.lld.exception;

public class CityAlreadyRegisteredException extends RuntimeException {

    public CityAlreadyRegisteredException(String message) {
        super(message);
    }
}
