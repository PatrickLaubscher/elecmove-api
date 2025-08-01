package fr.elecmove.api.repository;

import fr.elecmove.api.model.FavoriteStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, String> {
}
