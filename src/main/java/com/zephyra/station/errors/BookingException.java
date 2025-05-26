package com.zephyra.station.errors;

public class BookingException extends RuntimeException {
    public BookingException(String message) {
        super(message);
    }
}
