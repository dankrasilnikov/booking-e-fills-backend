package com.zephyra.station.service;

import com.zephyra.station.dto.StationCreateDTO;
import com.zephyra.station.errors.StationNotFoundException;
import com.zephyra.station.models.Connector;
import com.zephyra.station.models.Station;
import com.zephyra.station.repository.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class StationService {
    @Autowired
    StationRepository stationRepo;

    @Transactional
    public void addStation(StationCreateDTO dto) {
        if (dto.getConnectorCount() <= 0) {
            throw new IllegalArgumentException("Connector count must be positive");
        }
        Station station = new Station();
        station.setTitle(dto.getTitle());
        station.setLongitude(dto.getLongitude());
        station.setLatitude(dto.getLaitude());
        List<Connector> connectors = new ArrayList<>();
        for (int i = 1; i <= dto.getConnectorCount(); i++) {
            Connector connector = new Connector();
            connector.setSeqNum(i);
            connector.setStation(station);
            connectors.add(connector);
        }
        station.setConnectors(connectors);
        stationRepo.save(station);
    }

    @Transactional
    public void deleteStation(String title) {
        String formattedTitle = title.replace("_", " ");

        if (!stationRepo.existsByTitle(formattedTitle)) {
            throw new StationNotFoundException();
        }

        stationRepo.deleteByTitle(formattedTitle);
    }

    @Transactional(readOnly = true)
    public List<Station> getAllStations() {
        return stationRepo.findAll();
    }
}
