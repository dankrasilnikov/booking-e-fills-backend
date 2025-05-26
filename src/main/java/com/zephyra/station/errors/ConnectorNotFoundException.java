package com.zephyra.station.errors;

public class ConnectorNotFoundException extends  RuntimeException{
    public ConnectorNotFoundException(String message) {
        super(message);
    }
}
