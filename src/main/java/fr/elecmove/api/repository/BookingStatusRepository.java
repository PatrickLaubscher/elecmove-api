package fr.elecmove.api.repository;

import fr.elecmove.api.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingStatusRepository extends JpaRepository<BookingStatus, String> {
}
