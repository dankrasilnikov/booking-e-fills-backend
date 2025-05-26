package com.zephyra.station.errors;

public class BookingInThePastException extends  RuntimeException{
    public BookingInThePastException(String message) {
        super(message);
    }
}
