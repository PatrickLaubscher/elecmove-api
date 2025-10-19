package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.LocationStationBusiness;
import fr.elecmove.api.model.LocationStation;
import fr.elecmove.api.repository.LocationStationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@Transactional
public class LocationStationBusinessImpl implements LocationStationBusiness {

    LocationStationRepository locationStationRepository;

    public LocationStationBusinessImpl(LocationStationRepository locationStationRepository) {
        this.locationStationRepository = locationStationRepository;
    }

    @Override
    public LocationStation createLocation(LocationStation locationStation) {
        return locationStationRepository.save(locationStation);
    }

    @Override
    public LocationStation getLocation(String id) {
        return locationStationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The location does not exist")
        );
    }

    @Override
    public Optional<LocationStation> getLocationByLatitudeAndLongitude(BigDecimal latitude, BigDecimal longitude) {
        return locationStationRepository.findByLatitudeAndLongitude(latitude, longitude);
    }

    @Override
    public void deleteLocation(String id) {
        locationStationRepository.deleteById(id);
    }
}
