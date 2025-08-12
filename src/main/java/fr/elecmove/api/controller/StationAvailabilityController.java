package fr.elecmove.api.controller;


import fr.elecmove.api.business.StationAvailabilityBusiness;
import fr.elecmove.api.controller.dto.mapper.StationAvailabilityMapper;
import fr.elecmove.api.controller.dto.station_availability.StationAvailabilityCreationDTO;
import fr.elecmove.api.controller.dto.station_availability.StationAvailabilityDTO;
import fr.elecmove.api.model.StationAvailability;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class StationAvailabilityController {

    StationAvailabilityBusiness stationAvailabilityBusiness;
    StationAvailabilityMapper stationAvailabilityMapper;

    public StationAvailabilityController(StationAvailabilityBusiness stationAvailabilityBusiness, StationAvailabilityMapper stationAvailabilityMapper) {
        this.stationAvailabilityBusiness = stationAvailabilityBusiness;
        this.stationAvailabilityMapper = stationAvailabilityMapper;
    }


    @GetMapping("/{id}")
    public StationAvailabilityDTO getAvailability(@PathVariable String id) {
        return stationAvailabilityMapper.toDto(stationAvailabilityBusiness.getStationAvailability(id));
    }


    @GetMapping
    public List<StationAvailabilityDTO> getAllAvailabilities(@RequestParam String stationId) {
        List<StationAvailabilityDTO> availabilityDTOS = new ArrayList<>();
        for(StationAvailability availability : stationAvailabilityBusiness.getAllAvailabilityByStation(stationId)){
            availabilityDTOS.add(stationAvailabilityMapper.toDto(availability));
        }
        return availabilityDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_USER")
    public StationAvailabilityDTO createAvailability(@RequestBody @Valid StationAvailabilityCreationDTO dto) {
        return stationAvailabilityMapper.toDto(
                stationAvailabilityBusiness.createStationAvailability(stationAvailabilityMapper.toEntity(dto), dto.getStationId())
        );
    }


    @PatchMapping("/{id}")
    @Secured("ROLE_USER")
    public StationAvailabilityDTO updateAvailability(@PathVariable String id, @RequestBody StationAvailabilityCreationDTO dto) {
        return stationAvailabilityMapper.toDto(
                stationAvailabilityBusiness.updateStationAvailability(stationAvailabilityMapper.toEntity(dto), id)
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Secured("ROLE_USER")
    public void deleteUserAddress(@PathVariable String id) {
        stationAvailabilityBusiness.deleteStationAvailability(id);
    }


}
