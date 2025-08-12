package fr.elecmove.api.controller.dto.station_availability;


import com.fasterxml.jackson.annotation.JsonFormat;
import fr.elecmove.api.controller.dto.Station.StationDTO;

import java.time.LocalTime;

public class StationAvailabilityDTO {

    private String id;
    private String day;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startLocalTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endLocalTime;
    private StationDTO station;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public LocalTime getStartLocalTime() {
        return startLocalTime;
    }

    public void setStartLocalTime(LocalTime startLocalTime) {
        this.startLocalTime = startLocalTime;
    }

    public LocalTime getEndLocalTime() {
        return endLocalTime;
    }

    public void setEndLocalTime(LocalTime endLocalTime) {
        this.endLocalTime = endLocalTime;
    }

    public StationDTO getStation() {
        return station;
    }

    public void setStation(StationDTO station) {
        this.station = station;
    }
}
