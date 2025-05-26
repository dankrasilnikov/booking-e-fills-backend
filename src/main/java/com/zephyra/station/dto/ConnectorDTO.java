package com.zephyra.station.dto;

import com.zephyra.station.models.Connector;

public class ConnectorDTO {
    private Long id;
    private Long stationId;
    private Integer seqNum;
    private String status;

    public ConnectorDTO() {}

    public ConnectorDTO(Long id, Long stationId, Integer seqNum, String status) {
        this.id = id;
        this.stationId = stationId;
        this.seqNum = seqNum;
        this.status = status;
    }
    public static ConnectorDTO from(Connector connector) {
        return new ConnectorDTO(
                connector.getId(),
                connector.getStation() != null ? connector.getStation().getId() : null,
                connector.getSeqNum(),
                connector.getStatus().name()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStationId() {
        return stationId;
    }

    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }

    public Integer getSeqNum() {
        return seqNum;
    }

    public void setSeqNum(Integer seqNum) {
        this.seqNum = seqNum;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
