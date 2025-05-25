package com.zephyra.station.dto;

import com.vladmihalcea.hibernate.type.range.Range;
import com.zephyra.station.models.Reservation;

import java.time.*;
import java.util.UUID;

public class ReservationDTO {
    private String title;
    private Integer seqNum;
    private Long start;
    private Duration duration;
    private UUID uuid;

    public ReservationDTO() {}

    public ReservationDTO(String title, Integer seqNum, Long start,
                          Duration duration, UUID uuid) {
        this.title = title;
        this.seqNum = seqNum;
        this.start = start;
        this.duration = duration;
        this.uuid = uuid;
    }

    public static ReservationDTO from(Reservation reservation) {
        Range<ZonedDateTime> period = reservation.getPeriod();
        ZonedDateTime start = period.lower();
        ZonedDateTime end = period.upper();
        Duration duration = Duration.between(start, end);

        return new ReservationDTO(
                reservation.getConnector().getStation().getTitle(),
                reservation.getConnector().getSeqNum(),
                start.toInstant().getEpochSecond(),
                duration,
                reservation.getUuid()
        );
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public Long getStart() {
        return start;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setStart(Long start) {
        this.start = start;
    }

    public Duration getDuration() {
        return duration;
    }


    public void setDuration(Duration duration) {
        this.duration = duration;
    }

}
