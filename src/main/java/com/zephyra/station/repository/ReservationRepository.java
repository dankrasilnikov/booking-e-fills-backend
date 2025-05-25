package com.zephyra.station.repository;

import com.zephyra.station.models.Station;
import com.zephyra.station.models.Reservation;
import com.zephyra.station.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    List<Reservation> findAllByUser(User user);
    @Modifying
    @Query(value = "DELETE FROM reservation WHERE upper(period) < :now", nativeQuery = true)
    void deleteExpired(@Param("now") ZonedDateTime now);
    @Query(value = """
    SELECT * FROM reservation
    WHERE connector_id = :connectorId
      AND status IN ('HOLD', 'BOOKED', 'STARTED')
      AND period @> CAST(:instant AS timestamptz)
    """, nativeQuery = true)
    List<Reservation> findActiveAt(@Param("connectorId") Long connectorId,
                                   @Param("instant") ZonedDateTime instant);
}
