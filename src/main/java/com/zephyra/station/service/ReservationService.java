package com.zephyra.station.service;

import com.vladmihalcea.hibernate.type.range.Range;
import com.zephyra.station.dto.ReservationDTO;
import com.zephyra.station.errors.*;
import com.zephyra.station.models.*;
import com.zephyra.station.repository.ConnectorRepository;
import com.zephyra.station.repository.ReservationRepository;
import com.zephyra.station.repository.StationRepository;
import com.zephyra.station.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister;


import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReservationService {
    @Autowired
    UserRepository userRepo;
    @Autowired
    ConnectorRepository connectorRepo;
    @Autowired
    StationRepository stationRepo;
    @Autowired
    ReservationRepository reservationRepo;

    @Transactional
    public ReservationDTO book(ReservationDTO dto, String supabaseId) {
        Station station = stationRepo.findByTitle(dto.getTitle())
                .orElseThrow(() -> new StationNotFoundException());
        Connector connector = connectorRepo.findByStationIdAndSeqNum(station.getId(), dto.getSeqNum())
                .orElseThrow(() -> new ConnectorNotFoundException("Connector not found"));
        if (connector.getStatus() != ConnectorStatus.AVAILABLE)
            throw new IllegalStateException("Connector offline");
        ZonedDateTime start = Instant.ofEpochSecond(dto.getStart())
                .atZone(ZoneOffset.UTC);

        if (start.isBefore(ZonedDateTime.now(ZoneOffset.UTC))) {
            throw new BookingInThePastException("Reservation time cannot be in the past");
        }
        ZonedDateTime end = start.plus(dto.getDuration());

        Range<ZonedDateTime> period = Range.closedOpen(start, end);

        Reservation r = new Reservation();
        r.setConnector(connector);
        r.setUser(userRepo.findBySupabaseId(supabaseId).orElseThrow(UserNotFoundException::new));
        r.setPeriod(period);
        r.setStatus(ReservationStatus.BOOKED);

        try {
            return ReservationDTO.from(reservationRepo.save(r));
        } catch (DataIntegrityViolationException ex) {
            throw new SlotBusyException("Connector busy");
        }
    }

    public List<ReservationDTO> getUserReservations(String supabaseId) {
        User user = userRepo.findBySupabaseId(supabaseId)
                .orElseThrow(UserNotFoundException::new);

        return reservationRepo.findAllByUser(user).stream()
                .map(ReservationDTO::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void cancelReservation(UUID reservationUuid, String supabaseId) {
        Reservation reservation = reservationRepo.findById(reservationUuid)
                .orElseThrow(() -> new ReservationNotFoundException("Reservation not found"));

        if (!reservation.getUser().getSupabaseId().equals(supabaseId)) {
            throw new AccessDeniedException("You are not the owner of this reservation");
        }

        reservationRepo.delete(reservation);
    }
}
