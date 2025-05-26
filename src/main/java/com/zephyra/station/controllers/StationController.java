package com.zephyra.station.controllers;

import com.vladmihalcea.hibernate.type.range.Range;
import com.zephyra.station.dto.ConnectorDTO;
import com.zephyra.station.errors.BookingInThePastException;
import com.zephyra.station.models.ConnectorStatus;
import com.zephyra.station.models.Station;
import com.zephyra.station.repository.ConnectorRepository;

import com.zephyra.station.repository.ReservationRepository;
import com.zephyra.station.repository.StationRepository;
import com.zephyra.station.service.ReservationService;

import com.zephyra.station.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stations")
public class StationController {

    @Autowired
    ConnectorRepository connectorRepo;
    @Autowired
    ReservationRepository reservationRepo;
    @Autowired
    StationRepository stationRepository;
    @Autowired
    StationService stationService;

    /** GET http://localhost:8080/api/stations/2/free?timestamp=1747731600 */
    @GetMapping("/{id}/free")
    public List<ConnectorDTO> freeAt(@PathVariable Long id,
                                     @RequestParam Long timestamp) {
        ZonedDateTime start = Instant.ofEpochSecond(timestamp).atZone(ZoneOffset.UTC);
        ZonedDateTime end = start.plusSeconds(1);
        if (start.isBefore(ZonedDateTime.now(ZoneOffset.UTC))) {
            throw new BookingInThePastException("Reservation time cannot be in the past");
        }
        return connectorRepo.findFreeAt(id, start, end).stream()
                .map(ConnectorDTO::from)
                .toList();
    }
    @GetMapping("/free")
    public ResponseEntity<List<Station>> getFreeStationsAt(@RequestParam("timestamp") long timestamp) {
        ZonedDateTime instant = Instant.ofEpochSecond(timestamp).atZone(ZoneId.of("UTC"));

        List<Station> stations = stationRepository.findStationsWithFreeConnectorsAt(instant);

        for (Station station : stations) {
            station.setConnectors(
                    station.getConnectors().stream()
                            .filter(c -> c.getStatus() == ConnectorStatus.AVAILABLE &&
                                    reservationRepo.findActiveAt(c.getId(), instant).isEmpty())
                            .collect(Collectors.toList())
            );
        }

        return ResponseEntity.ok(stations);
    }

    @GetMapping("/getall")
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }

}
