package fr.elecmove.api.repository;

import fr.elecmove.api.model.StationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationExceptionRepository extends JpaRepository<StationException, String> {

    List<StationException> findByStationId(String id);

}
