package fr.elecmove.api.business;

import fr.elecmove.api.model.LocationStation;

import java.math.BigDecimal;
import java.util.Optional;


public interface LocationStationBusiness {


    /**
     *
     * @param locationStation
     * @return
     */
    LocationStation createLocation(LocationStation locationStation);


    /**
     *
     * @param id
     * @return
     */
    LocationStation getLocation(String id);


    /**
     *
     * @param latitude
     * @param longitude
     * @return
     */
    Optional<LocationStation> getLocationByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude);


    /**
     * Delete location after verifying that there is no station at this location
     *
     * @param id
     * @return
     */
    void deleteLocation(String id);
}
