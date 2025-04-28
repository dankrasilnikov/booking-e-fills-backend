package com.example.station.models;

import java.time.LocalDateTime;

public class TimeSlotAvailability {
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean available;

    public TimeSlotAvailability(LocalDateTime startTime, LocalDateTime endTime, boolean available) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.available = available;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
