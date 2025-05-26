package com.zephyra.station.models;

import com.vladmihalcea.hibernate.type.range.PostgreSQLRangeType;
import com.vladmihalcea.hibernate.type.range.Range;

import jakarta.persistence.*;
import org.hibernate.annotations.Type;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.UUID;


@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY) private User user;
    @ManyToOne(fetch = FetchType.LAZY) private Connector connector;

    @Type(PostgreSQLRangeType.class)
    @Column(columnDefinition = "tstzrange", nullable = false)
    private Range<ZonedDateTime> period;

    @Enumerated(EnumType.STRING)
    private ReservationStatus status = ReservationStatus.HOLD;


    public Reservation() {}


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Connector getConnector() {
        return connector;
    }

    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    public Range<ZonedDateTime> getPeriod() {
        return period;
    }

    public void setPeriod(Range<ZonedDateTime> period) {
        this.period = period;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

}
