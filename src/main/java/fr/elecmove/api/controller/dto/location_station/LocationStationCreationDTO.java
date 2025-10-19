package fr.elecmove.api.controller.dto.location_station;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public class LocationStationCreationDTO {

    @NotBlank
    @Size(min = 4,max = 120)
    private String address;
    @NotBlank
    @Size(min = 4,max = 64)
    private String city;
    @NotBlank
    @Length(max = 5)
    private String zipcode;
    @NotNull
    private BigDecimal latitude;
    @NotNull
    private BigDecimal longitude;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }
}
