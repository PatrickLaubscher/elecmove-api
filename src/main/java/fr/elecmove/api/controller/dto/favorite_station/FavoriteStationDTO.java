package fr.elecmove.api.controller.dto.favorite_station;

import fr.elecmove.api.controller.dto.station.StationDTO;
import fr.elecmove.api.controller.dto.user.UserSingleDTO;

public class FavoriteStationDTO {

    private UserSingleDTO user;
    private StationDTO station;


    public UserSingleDTO getUser() {
        return user;
    }

    public void setUser(UserSingleDTO user) {
        this.user = user;
    }

    public StationDTO getStation() {
        return station;
    }

    public void setStation(StationDTO station) {
        this.station = station;
    }
}
