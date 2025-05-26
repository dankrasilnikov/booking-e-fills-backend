package com.zephyra.station.errors;

public class StationNotFoundException extends RuntimeException {
    public StationNotFoundException() {
        super("Station not found");
    }
}
