package fr.elecmove.api.controller.dto.station;

import fr.elecmove.api.controller.dto.location_station.LocationStationDTO;

import java.time.LocalDateTime;

public class StationSimpleDTO {

    private String id;
    private String name;
    private Double tarification;
    private String power;
    private String instruction;
    private Boolean freeStanding;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocationStationDTO location;

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

    public LocationStationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationStationDTO location) {
        this.location = location;
    }
}