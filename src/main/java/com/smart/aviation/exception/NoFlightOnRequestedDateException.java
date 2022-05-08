package com.smart.aviation.exception;

public class NoFlightOnRequestedDateException extends IllegalArgumentException {

    public NoFlightOnRequestedDateException(String s) {
        super(s);
    }
}
