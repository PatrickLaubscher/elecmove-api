package fr.elecmove.api.repository;

import fr.elecmove.api.model.StationAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationAvailabilityRepository extends JpaRepository<StationAvailability, String> {
}
