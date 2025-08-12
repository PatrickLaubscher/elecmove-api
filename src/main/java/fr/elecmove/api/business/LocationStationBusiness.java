package fr.elecmove.api.business;

import fr.elecmove.api.model.LocationStation;


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
     * Delete location after verifying that there is no station at this location
     *
     * @param id
     * @return
     */
    void deleteLocation(String id);
}
