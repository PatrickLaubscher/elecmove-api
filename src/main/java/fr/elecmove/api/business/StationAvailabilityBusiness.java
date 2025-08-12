package fr.elecmove.api.business;

import fr.elecmove.api.model.StationAvailability;

import java.util.List;

public interface StationAvailabilityBusiness {


    /**
     *
     * @param availability
     * @param stationId
     * @return
     */
    StationAvailability createStationAvailability(StationAvailability availability, String stationId);


    /**
     *
     * @param id
     * @return
     */
    StationAvailability getStationAvailability(String id);


    /**
     *
     * @param stationId
     * @return
     */
    List<StationAvailability> getAllAvailabilityByStation(String stationId);


    /**
     *
     * @param availability
     * @param availabilityId
     * @return
     */
    StationAvailability updateStationAvailability(StationAvailability availability, String availabilityId);


    /**
     *
     * @param id
     * @return
     */
    void deleteStationAvailability(String id);

}
