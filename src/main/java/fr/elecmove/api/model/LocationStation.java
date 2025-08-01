package fr.elecmove.api.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "location_station", schema = "elecmove")
public class LocationStation {

    @Id
    @UuidGenerator
    private String id;
    private String address;
    private String city;
    private String zipcode;
    private Double latitude;
    private Double longitude;

    public LocationStation(String id, String address, String city, String zipcode, Double latitude, Double longitude) {
        this.id = id;
        this.address = address;
        this.city = city;
        this.zipcode = zipcode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationStation() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
