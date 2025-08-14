package fr.elecmove.api.repository;

import fr.elecmove.api.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, String> {

    List<Station> findStationByUserEmail(String email);

}
