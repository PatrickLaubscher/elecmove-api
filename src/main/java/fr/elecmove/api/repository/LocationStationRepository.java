package fr.elecmove.api.repository;

import fr.elecmove.api.model.LocationStation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LocationStationRepository extends JpaRepository<LocationStation, String> {
    Optional<LocationStation> findByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);
}
