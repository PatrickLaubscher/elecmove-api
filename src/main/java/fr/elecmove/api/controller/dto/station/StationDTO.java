package fr.elecmove.api.controller.dto.station;

import fr.elecmove.api.controller.dto.location_station.LocationStationDTO;
import fr.elecmove.api.controller.dto.station_availability.StationAvailabilityDTO;
import fr.elecmove.api.controller.dto.user.UserSingleDTO;


import java.time.LocalDateTime;
import java.util.List;

public class StationDTO {

    private String id;
    private String name;
    private Double tarification;
    private String power;
    private String instruction;
    private Boolean freeStanding;
    private Boolean available;
    private String type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSingleDTO user;
    private LocationStationDTO location;
    private List<StationAvailabilityDTO> availabilities;

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

    public Boolean isFreeStanding() {
        return freeStanding;
    }

    public void setFreeStanding(Boolean freeStanding) {
        this.freeStanding = freeStanding;
    }

    public Boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public LocationStationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationStationDTO location) {
        this.location = location;
    }

    public List<StationAvailabilityDTO> getAvailabilities() {
        return availabilities;
    }

    public void setAvailabilities(List<StationAvailabilityDTO> availabilities) {
        this.availabilities = availabilities;
    }
}
