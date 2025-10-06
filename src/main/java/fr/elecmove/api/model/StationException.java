package fr.elecmove.api.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalTime;


@Entity
@Table(name = "station_exception")
public class StationException {

    @Id
    @UuidGenerator
    private String id;
    @Column(name = "exception_day")
    private String day;
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time")
    private LocalTime startLocalTime;
    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time")
    private LocalTime endLocalTime;

    @ManyToOne
    private Station station;


    public StationException() {
    }


    public StationException(String id, String day, LocalTime startLocalTime, LocalTime endLocalTime, Station station) {
        this.id = id;
        this.day = day;
        this.startLocalTime = startLocalTime;
        this.endLocalTime = endLocalTime;
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

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }
}
