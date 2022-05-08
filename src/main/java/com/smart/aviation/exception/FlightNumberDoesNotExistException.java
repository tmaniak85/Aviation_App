package com.smart.aviation.exception;

public class FlightNumberDoesNotExistException extends IllegalArgumentException{

    public FlightNumberDoesNotExistException(String s) {
        super(s);
    }

}
