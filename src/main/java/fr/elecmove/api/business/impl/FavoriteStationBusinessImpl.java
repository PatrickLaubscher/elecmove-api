package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.FavoriteStationBusiness;
import fr.elecmove.api.business.StationBusiness;
import fr.elecmove.api.model.FavoriteStation;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.FavoriteStationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@Service
@Transactional
public class FavoriteStationBusinessImpl implements FavoriteStationBusiness {

    StationBusiness stationBusiness;
    FavoriteStationRepository favoriteStationRepository;

    public FavoriteStationBusinessImpl(StationBusiness stationBusiness, FavoriteStationRepository favoriteStationRepository) {
        this.stationBusiness = stationBusiness;
        this.favoriteStationRepository = favoriteStationRepository;
    }

    @Override
    public FavoriteStation createFavoriteStation(String stationId, User user) {
        Station station = stationBusiness.getStation(stationId);

        if (favoriteStationRepository.existsByUserAndStation(user, station)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Station already marked as favorite"
            );
        }

        FavoriteStation favoriteStation = new FavoriteStation(user, station);
        return favoriteStationRepository.save(favoriteStation);
    }

    @Override
    public FavoriteStation getFavoriteStation(String id) {
        return favoriteStationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The favorite does not exist")
        );
    }

    @Override
    public List<FavoriteStation> getAllFavouriteStation(String userEmail) {
        return favoriteStationRepository.findAllByUserEmail(userEmail);
    }

    @Override
    public void deleteFavoriteStation(String id, User user) {

        FavoriteStation existingFavorite = getFavoriteStation(id);

        if(!existingFavorite.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this favorite station.");
        }

        favoriteStationRepository.delete(existingFavorite);

    }
}
