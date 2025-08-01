package fr.elecmove.api.repository;

import fr.elecmove.api.model.LocationStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationStationRepository extends JpaRepository<LocationStation, String> {
}
