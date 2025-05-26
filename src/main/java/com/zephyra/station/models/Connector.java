package com.zephyra.station.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "connector",
        uniqueConstraints = @UniqueConstraint(columnNames = {"station_id","seq_num"}))
public class Connector {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "station_id")
    @JsonBackReference
    private Station station;

    @Column(name = "seq_num")
    private Integer seqNum;

    @Enumerated(EnumType.STRING)
    private ConnectorStatus status = ConnectorStatus.AVAILABLE;
    public Connector(){}
    public Connector(Long id, Station station, Integer seqNum) {
        this.id = id;
        this.station = station;
        this.seqNum = seqNum;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public ConnectorStatus getStatus() {
        return status;
    }

    public void setStatus(ConnectorStatus status) {
        this.status = status;
    }
}
