package com.zephyra.station.dto;

public class StationCreateDTO {
    private String title;
    private int connectorCount;
    private double latitude;
    private double longitude;

    // Геттеры/сеттеры
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getConnectorCount() {
        return connectorCount;
    }

    public void setConnectorCount(int connectorCount) {
        this.connectorCount = connectorCount;
    }

    public double getLaitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}

