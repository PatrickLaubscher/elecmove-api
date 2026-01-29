package fr.elecmove.api.repository;

import fr.elecmove.api.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface StationRepository extends JpaRepository<Station, String> {

    @Query("SELECT s FROM Station s LEFT JOIN FETCH s.exceptions WHERE s.id = :id")
    Station findOneStationByIdWithExceptions(@Param("id") String id);

    @Query("SELECT s FROM Station s LEFT JOIN FETCH s.exceptions WHERE s.user.email = :email")
    List<Station> findStationByUserEmailWithExceptions(@Param("email") String email);

    @Query(value = """
    SELECT s.*
    FROM station s
    JOIN location_station ls ON s.location_id = ls.id
    WHERE s.is_available = TRUE
      AND ST_Distance_Sphere(
            POINT(ls.longitude, ls.latitude),
            POINT(:longitude, :latitude)
          ) <= :rayon
    ORDER BY ST_Distance_Sphere(
            POINT(ls.longitude, ls.latitude),
            POINT(:longitude, :latitude)
          ) ASC
    """, nativeQuery = true)
    List<Station> findStationsNearby(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("rayon") double rayonMeters
    );


    @Query("""
    SELECT s
    FROM Station s
    LEFT JOIN s.exceptions se
      ON se.day = :weekday
      AND se.startLocalTime <= :startTime
      AND se.endLocalTime >= :endTime
    WHERE se.id IS NULL
    """)
    List<Station> findAvailableStationsByDayAndTime(
            @Param("weekday") String weekday,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


    @Query("""
    SELECT s
    FROM Station s
    WHERE s.id IN :stationIds
    AND NOT EXISTS (
        SELECT b
        FROM Booking b
        WHERE b.station = s
          AND b.date = :date
          AND (:startTime < b.endTime AND :endTime > b.startTime)
    )
    """)
    List<Station> filterStationsWithoutBooking(
            @Param("stationIds") List<String> stationIds,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );


    @Query("""
    SELECT s
    FROM Station s
    LEFT JOIN s.exceptions se
        ON se.day = :weekday
        AND se.startLocalTime <= :startTime
        AND se.endLocalTime >= :endTime
    LEFT JOIN Booking b
        ON b.station = s
        AND b.date = :date
        AND b.startTime < :endTime
        AND b.endTime > :startTime
    WHERE s.id = :stationId
      AND se.id IS NULL
      AND b.id IS NULL
""")
    Optional<Station> findAvailableStationByIdAndTime(
            @Param("stationId") String stationId,
            @Param("weekday") String weekday,
            @Param("date") LocalDate date,
            @Param("startTime") LocalTime startTime,
            @Param("endTime") LocalTime endTime
    );

}
