package fr.elecmove.api.controller.dto.car;


import jakarta.validation.constraints.NotBlank;

public class CarCreationDTO {

    @NotBlank
    private String type;
    @NotBlank
    private String registration;
    @NotBlank
    private String brand;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
