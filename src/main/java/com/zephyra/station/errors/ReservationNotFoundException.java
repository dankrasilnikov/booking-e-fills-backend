package com.zephyra.station.errors;
public class ReservationNotFoundException extends  RuntimeException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
