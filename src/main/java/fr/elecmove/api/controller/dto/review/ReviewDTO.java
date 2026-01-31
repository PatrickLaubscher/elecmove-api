package fr.elecmove.api.controller.dto.review;

import fr.elecmove.api.controller.dto.user.UserSingleDTO;

import java.time.LocalDateTime;

public class ReviewDTO {

    private String id;
    private String comment;
    private Integer rate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSingleDTO user;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserSingleDTO getUser() {
        return user;
    }

    public void setUser(UserSingleDTO user) {
        this.user = user;
    }
}
