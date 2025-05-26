package com.zephyra.station.errors;

public class SlotBusyException extends  RuntimeException {
    public SlotBusyException(String message) {
        super(message);
    }
}
