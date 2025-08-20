package fr.elecmove.api.model;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FavoriteStationId that = (FavoriteStationId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(stationId, that.stationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, stationId);
    }
}
