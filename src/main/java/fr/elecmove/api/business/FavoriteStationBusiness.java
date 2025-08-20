package fr.elecmove.api.business;

import fr.elecmove.api.model.FavoriteStation;
import fr.elecmove.api.model.User;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

public interface FavoriteStationBusiness {



    /**
     *
     * @param stationId
     * @param user
     * @return
     */
    FavoriteStation createFavoriteStation(String stationId, User user);


    /**
     *
     *
     * @param id
     * @return
     */
    FavoriteStation getFavoriteStation(String id);



    /**
     *
     * @param userEmail
     * @return
     */
    List<FavoriteStation> getAllFavouriteStation(String userEmail);



    /**
     *
     * @param id
     * @return
     */
    void deleteFavoriteStation(String id, User user);

}
