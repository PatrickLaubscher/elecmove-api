package fr.elecmove.api.model;


import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "station")
public class Station {

    @Id
    @UuidGenerator
    private String id;
    private String name;
    private Double tarification;
    private String power;
    private String instruction;

    @Column(name = "is_free_standing")
    private Boolean freeStanding;

    @Column(name = "is_available")
    private Boolean available;

    private String type;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private LocationStation location;

    @OneToMany(mappedBy = "station")
    private List<StationException> exceptions = new ArrayList<>();

    @OneToMany(mappedBy = "station")
    private List<Picture> pictures = new ArrayList<>();

    public Station(String id, String name, Double tarification, String power, String instruction, Boolean freeStanding, Boolean available, String type, LocalDateTime createdAt, LocalDateTime updatedAt, User user, LocationStation location) {
        this.id = id;
        this.name = name;
        this.tarification = tarification;
        this.power = power;
        this.instruction = instruction;
        this.freeStanding = freeStanding;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.user = user;
        this.location = location;
    }

    public Station() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getTarification() {
        return tarification;
    }

    public void setTarification(Double tarification) {
        this.tarification = tarification;
    }

    public String getPower() {
        return power;
    }

    public void setPower(String power) {
        this.power = power;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Boolean getFreeStanding() {
        return freeStanding;
    }

    public void setFreeStanding(Boolean freeStanding) {
        this.freeStanding = freeStanding;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocationStation getLocation() {
        return location;
    }

    public void setLocation(LocationStation location) {
        this.location = location;
    }

    public List<StationException> getAvailabilities() {
        return exceptions;
    }

    public void setAvailabilities(List<StationException> exceptions) {
        this.exceptions = exceptions;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }
}
