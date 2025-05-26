package com.zephyra.station.schedule;

import com.zephyra.station.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Component
public class ReservationCleanupScheduler {
    @Autowired
    ReservationRepository reservationRepo;
    @Scheduled(fixedDelay = 60 * 60 * 1000)
    @Transactional
    public void deleteExpiredReservations() {
        reservationRepo.deleteExpired(ZonedDateTime.now(ZoneOffset.UTC));
    }
}
