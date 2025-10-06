package fr.elecmove.api.repository;

import fr.elecmove.api.model.Booking;
import fr.elecmove.api.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByUserEmail(String email);

    List<Booking> findByStationId(String stationId);

    List<Booking> findByStationIdAndStatusId(String stationId, int statusId);

    @Query("""
    SELECT COUNT(b) > 0
    FROM Booking b
    WHERE b.date = :date
      AND ( :startTime < b.endTime AND :endTime > b.startTime )
    """)
    boolean checkExistingBooking(@Param("date") LocalDate date,
                          @Param("startTime") LocalTime startTime,
                          @Param("endTime") LocalTime endTime);

}
