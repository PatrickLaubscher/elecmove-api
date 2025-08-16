package fr.elecmove.api.repository;

import fr.elecmove.api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, String> {

    List<Booking> findByUserEmail(String email);

}
