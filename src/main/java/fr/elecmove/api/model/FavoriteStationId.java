package fr.elecmove.api.model;

import java.io.Serializable;

public class FavoriteStationId implements Serializable {

    private String userId;
    private String stationId;

    public FavoriteStationId() {}

    public FavoriteStationId(String userId, String stationId) {
        this.userId = userId;
        this.stationId = stationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
