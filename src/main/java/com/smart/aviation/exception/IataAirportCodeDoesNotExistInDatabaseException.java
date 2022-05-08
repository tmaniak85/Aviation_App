package com.smart.aviation.exception;

public class IataAirportCodeDoesNotExistInDatabaseException extends IllegalArgumentException {

    public IataAirportCodeDoesNotExistInDatabaseException(String s) {
        super(s);
    }
}
