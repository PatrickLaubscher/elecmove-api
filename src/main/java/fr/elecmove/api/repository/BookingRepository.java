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

    @Query("""
    SELECT b FROM Booking b
    WHERE b.user.email = :email
    AND b.date >= CURRENT_DATE
    """)
    List<Booking> findUpcomingBookingsByUserEmail(@Param("email") String email);

    @Query("""
    SELECT b FROM Booking b
    WHERE b.user.email = :email
    AND b.date < CURRENT_DATE
    ORDER BY b.date DESC
    """)
    List<Booking> findPastBookingsByUserEmail(@Param("email") String email);

    @Query("""
    SELECT b
    FROM Booking b
    WHERE b.user = :email 
    AND b.status.id = :statusId
    """)
    List<Booking> findByUserEmailAndStatusId(@Param("email") String email, @Param("statusId") int statusId);

    @Query("""
    SELECT b
    FROM Booking b
    WHERE b.station.user.email = :email
    AND b.status.id = :statusId
    """)
    List<Booking> findByStationOwnerEmailAndStatusId(@Param("email") String email, @Param("statusId") int statusId);

}
