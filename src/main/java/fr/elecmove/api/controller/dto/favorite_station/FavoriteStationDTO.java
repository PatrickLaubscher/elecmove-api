package fr.elecmove.api.controller.dto.favorite_station;

import fr.elecmove.api.controller.dto.station.StationSimpleDTO;
import fr.elecmove.api.controller.dto.user.UserSingleDTO;

public class FavoriteStationDTO {

    private String id;
    private UserSingleDTO user;
    private StationSimpleDTO station;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserSingleDTO getUser() {
        return user;
    }

    public void setUser(UserSingleDTO user) {
        this.user = user;
    }

    public StationSimpleDTO getStation() {
        return station;
    }

    public void setStation(StationSimpleDTO station) {
        this.station = station;
    }
}
