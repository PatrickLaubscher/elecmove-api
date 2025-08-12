package fr.elecmove.api.repository;

import fr.elecmove.api.model.StationAvailability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationAvailabilityRepository extends JpaRepository<StationAvailability, String> {

    List<StationAvailability> findByStationId(String stationId);

}
