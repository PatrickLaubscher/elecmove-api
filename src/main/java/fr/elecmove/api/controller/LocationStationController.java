package fr.elecmove.api.controller;


import fr.elecmove.api.business.LocationStationBusiness;
import fr.elecmove.api.controller.dto.location_station.LocationStationCreationDTO;
import fr.elecmove.api.controller.dto.location_station.LocationStationDTO;
import fr.elecmove.api.controller.dto.mapper.LocationMapper;
import fr.elecmove.api.model.LocationStation;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/locations")
public class LocationStationController {

    LocationStationBusiness locationStationBusiness;
    LocationMapper locationMapper;

    public LocationStationController(LocationStationBusiness locationStationBusiness, LocationMapper locationMapper) {
        this.locationStationBusiness = locationStationBusiness;
        this.locationMapper = locationMapper;
    }

    @GetMapping("/{id}")
    public LocationStationDTO getLocation(@PathVariable String id) {
        return locationMapper.toDto(locationStationBusiness.getLocation(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_USER")
    public LocationStationDTO createLocation(@RequestBody @Valid LocationStationCreationDTO dto) {
        LocationStation location = locationStationBusiness
                .getLocationByLatitudeAndLongitude(dto.getLatitude(), dto.getLongitude())
                .orElseGet(() -> locationStationBusiness.createLocation(locationMapper.toEntity(dto)));

        return locationMapper.toDto(location);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_USER")
    public void deleteLocation(@PathVariable String id) {
        locationStationBusiness.deleteLocation(id);
    }

}
