package com.smart.aviation.exception;

public class NoDateOrWrongDateFormat extends IllegalArgumentException {

    public NoDateOrWrongDateFormat(String s) {
        super(s);
    }
}
