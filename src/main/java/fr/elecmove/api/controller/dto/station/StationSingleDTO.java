package fr.elecmove.api.controller.dto.station;


import fr.elecmove.api.controller.dto.location_station.LocationStationDTO;

public class StationSingleDTO {

    private String id;
    private String name;
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

    public LocationStationDTO getLocation() {
        return location;
    }

    public void setLocation(LocationStationDTO location) {
        this.location = location;
    }

}
