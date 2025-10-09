package fr.elecmove.api.business;

import fr.elecmove.api.model.StationException;

import java.util.List;

public interface StationExceptionBusiness {


    /**
     *
     * @param availability
     * @param stationId
     * @return
     */
    StationException createStationAvailability(StationException availability, String stationId);


    /**
     *
     * @param id
     * @return
     */
    StationException getStationAvailability(String id);


    /**
     *
     * @param stationId
     * @return
     */
    List<StationException> getAllAvailabilityByStation(String stationId);


    /**
     *
     * @param availability
     * @param availabilityId
     * @return
     */
    StationException updateStationAvailability(StationException availability, String availabilityId);


    /**
     *
     * @param id
     * @return
     */
    void deleteStationAvailability(String id);

}
