package fr.elecmove.api.repository;

import fr.elecmove.api.model.FavoriteStation;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FavoriteStationRepository extends JpaRepository<FavoriteStation, String> {

    boolean existsByUserAndStation(User user, Station station);
    List<FavoriteStation> findAllByUserEmail(String email);

}
