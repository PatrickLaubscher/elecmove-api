package fr.elecmove.api.controller.dto.station_exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;


public class StationExceptionCreationDTO {

    @NotBlank
    private String day;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startLocalTime;
    @NotNull
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endLocalTime;
    @NotBlank
    private String stationId;

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

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }
}
