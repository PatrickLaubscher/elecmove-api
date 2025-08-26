package fr.elecmove.api.business;

import fr.elecmove.api.model.LocationStation;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface StationBusiness {


    /**
     *
     * @param station
     * @param user
     * @return
     */
    Station createStation(Station station, LocationStation location, User user);


    /**
     *
     * @param id
     * @return
     */
    Station getStation(String id);


    /**
     *
     * @param email
     * @return
     */
    List<Station> getAllStationByEmail(String email);


    /**
     *
     * @param latitude
     * @param longitude
     * @param rayonMeters
     * @return
     */
    List<Station> getAllStationByLocation(double latitude, double longitude, double rayonMeters);


    /**
     *
     * @param date
     * @param startTime
     * @param endTime
     * @return
     */
    List<Station> checkStationBookingAvailability(LocalDate date, LocalTime startTime, LocalTime endTime);


    /**
     *
     * @param id
     * @param station
     * @param user
     * @return
     */
    Station updateStation(String id, Station station, User user);



    /**
     *
     * @param id
     * @return
     */
    void deleteStation(String id, User user);

}
