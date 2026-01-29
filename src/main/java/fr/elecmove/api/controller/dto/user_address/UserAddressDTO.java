package fr.elecmove.api.controller.dto.user_address;

import fr.elecmove.api.controller.dto.user.UserSingleDTO;



public class UserAddressDTO {

    private String id;
    private String addressName;
    private String address;
    private String city;
    private String zipcode;
    private Double latitude;
    private Double longitude;
    private UserSingleDTO user;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddressName() {
        return addressName;
    }

    public void setAddressName(String addressName) {
        this.addressName = addressName;
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

    public UserSingleDTO getUser() {
        return user;
    }

    public void setUser(UserSingleDTO user) {
        this.user = user;
    }
}
