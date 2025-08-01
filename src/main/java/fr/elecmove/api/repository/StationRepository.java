package fr.elecmove.api.repository;

import fr.elecmove.api.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StationRepository extends JpaRepository<Station, String> {
}
