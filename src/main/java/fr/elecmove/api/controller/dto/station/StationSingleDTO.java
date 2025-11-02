package fr.elecmove.api.controller.dto.station;


import fr.elecmove.api.controller.dto.location_station.LocationStationDTO;

public class StationSingleDTO {

    private String id;
    private String name;
    private Double tarification;
    private String power;
    private Boolean available;
    private Boolean freeStanding;
    private LocationStationDTO location;
    private Boolean availableAtGivenSlot;

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Boolean getFreeStanding() {
        return freeStanding;
    }

    public void setFreeStanding(Boolean freeStanding) {
        this.freeStanding = freeStanding;
    }

    public Boolean getAvailableAtGivenSlot() {
        return availableAtGivenSlot;
    }

    public void setAvailableAtGivenSlot(Boolean availableAtGivenSlot) {
        this.availableAtGivenSlot = availableAtGivenSlot;
    }

    public LocationStationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationStationDTO location) {
        this.location = location;
    }

}
