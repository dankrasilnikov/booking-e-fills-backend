package com.zephyra.station.repository;

import com.zephyra.station.models.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StationRepository extends JpaRepository<Station, Long> {
    Optional<Station> findByTitle(String title);
    @Modifying
    void deleteByTitle(String title);
    boolean existsByTitle(String title);
    @Query(value = """
    SELECT DISTINCT s.* FROM station s
    JOIN connector c ON c.station_id = s.id
    WHERE c.status = 'AVAILABLE'
      AND NOT EXISTS (
          SELECT 1 FROM reservation r
          WHERE r.connector_id = c.id
            AND r.status IN ('HOLD', 'BOOKED', 'STARTED')
            AND r.period @> CAST(:instant AS timestamptz)
      )
    """, nativeQuery = true)
    List<Station> findStationsWithFreeConnectorsAt(@Param("instant") ZonedDateTime instant);
}
