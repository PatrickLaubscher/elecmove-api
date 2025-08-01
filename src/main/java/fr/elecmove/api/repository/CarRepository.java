package fr.elecmove.api.repository;

import fr.elecmove.api.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Booking, String> {
}
