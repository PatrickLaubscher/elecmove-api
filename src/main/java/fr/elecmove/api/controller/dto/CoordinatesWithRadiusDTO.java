package fr.elecmove.api.controller.dto;


import jakarta.validation.constraints.NotNull;

public class CoordinatesWithRadiusDTO {

    @NotNull
    private double latitude;
    @NotNull
    private double longitude;
    @NotNull
    private double rayonMeters;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getRayonMeters() {
        return rayonMeters;
    }

    public void setRayonMeters(double rayonMeters) {
        this.rayonMeters = rayonMeters;
    }
}
