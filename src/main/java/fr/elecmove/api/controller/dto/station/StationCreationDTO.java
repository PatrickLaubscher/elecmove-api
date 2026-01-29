package fr.elecmove.api.controller.dto.station;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class StationCreationDTO {

    @NotNull
    @Size(min = 1, max = 30)
    private String name;
    @NotNull
    private Double tarification;
    @NotBlank
    private String power;
    @Size(max = 120)
    private String instruction;
    @NotNull
    private Boolean freeStanding;
    @NotNull
    private Boolean available;
    @NotBlank
    private String locationStationId;

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

    public String getLocationStationId() {
        return locationStationId;
    }

    public void setLocationStationId(String locationStationId) {
        this.locationStationId = locationStationId;
    }
}
