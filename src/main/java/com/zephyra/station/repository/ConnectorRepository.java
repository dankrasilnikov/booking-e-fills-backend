package com.zephyra.station.repository;

import com.vladmihalcea.hibernate.type.range.Range;
import com.zephyra.station.models.Connector;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectorRepository extends JpaRepository<Connector, Long> {
@Query(value = """
        SELECT c.* FROM connector c
        WHERE c.station_id = :stationId
          AND c.status = 'AVAILABLE'
          AND NOT EXISTS (
              SELECT 1 FROM reservation r
              WHERE r.connector_id = c.id
                AND r.status IN ('HOLD','BOOKED','STARTED')
                AND r.period && tstzrange(:start, :end)
          )
        """, nativeQuery = true)
List<Connector> findFreeAt(@Param("stationId") Long stationId,
                           @Param("start") ZonedDateTime start,
                           @Param("end") ZonedDateTime end);

    Optional<Connector> findByStationIdAndSeqNum(Long stationId, Integer seqNum);
}
