package fr.elecmove.api.model;


import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.sql.Time;

@Entity
@Table(name = "station_availability", schema = "elecmove")
public class StationAvailability {

    @Id
    @UuidGenerator
    private String id;
    private String day;
    @Column(name = "start_time")
    private Time startTime;
    @Column(name = "end_time")
    private Time endTime;

    @ManyToOne
    private Station station;


    public StationAvailability() {
    }


    public StationAvailability(String id, String day, Time startTime, Time endTime, Station station) {
        this.id = id;
        this.day = day;
        this.startTime = startTime;
        this.endTime = endTime;
        this.station = station;
    }

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

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
