package fr.elecmove.api.model;


import jakarta.persistence.*;

@Entity
@Table(name = "favorite_station", schema = "elecmove")
@IdClass(FavoriteStation.class)
public class FavoriteStation {

    @Id
    @Column(name="user_id")
    private String userId;

    @Id
    @Column(name="group_id")
    private String groupId;

    public FavoriteStation() {
    }

    public FavoriteStation(String userId, String groupId) {
        this.userId = userId;
        this.groupId = groupId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
