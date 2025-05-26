package com.zephyra.station.controllers;

import com.zephyra.station.dto.StationCreateDTO;
import com.zephyra.station.models.Station;
import com.zephyra.station.repository.StationRepository;
import com.zephyra.station.service.StationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")

public class AdminController {

    @Autowired
    StationRepository gasStationRepository;
    @Autowired
    StationService stationService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/stations/add")
    public ResponseEntity addStation(@RequestBody StationCreateDTO dto) {
        stationService.addStation(dto);
        return  ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/stations/delete/{title}")
    public ResponseEntity deleteStation(@PathVariable String title) {
        stationService.deleteStation(title);
        return  ResponseEntity.ok().build();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/stations")
    public ResponseEntity<List<Station>> getAllStations() {
        return ResponseEntity.ok(stationService.getAllStations());
    }
}