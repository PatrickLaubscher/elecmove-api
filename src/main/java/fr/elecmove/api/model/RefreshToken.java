package fr.elecmove.api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
public class RefreshToken {

    @Id
    @UuidGenerator
    private String id;
    private LocalDateTime expiresAt;

    @ManyToOne
    private User user;


    public RefreshToken() {
    }


    public RefreshToken(String id, LocalDateTime expiresAt, User user) {
        this.id = id;
        this.expiresAt = expiresAt;
        this.user = user;
    }


    public boolean isExpired() {
        return expiresAt.isBefore(LocalDateTime.now());
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }


    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }


    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }

}
