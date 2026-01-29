package fr.elecmove.api.controller;


import fr.elecmove.api.business.StationExceptionBusiness;
import fr.elecmove.api.controller.dto.mapper.StationAvailabilityMapper;
import fr.elecmove.api.controller.dto.station_exception.StationExceptionCreationDTO;
import fr.elecmove.api.controller.dto.station_exception.StationExceptionDTO;
import fr.elecmove.api.model.StationException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/availabilities")
public class StationExceptionController {

    StationExceptionBusiness stationAvailabilityBusiness;
    StationAvailabilityMapper stationAvailabilityMapper;

    public StationExceptionController(StationExceptionBusiness stationAvailabilityBusiness, StationAvailabilityMapper stationAvailabilityMapper) {
        this.stationAvailabilityBusiness = stationAvailabilityBusiness;
        this.stationAvailabilityMapper = stationAvailabilityMapper;
    }


    @GetMapping("/{id}")
    public StationExceptionDTO getAvailability(@PathVariable String id) {
        return stationAvailabilityMapper.toDto(stationAvailabilityBusiness.getStationAvailability(id));
    }


    @GetMapping
    public List<StationExceptionDTO> getAllAvailabilities(@RequestParam String stationId) {
        List<StationExceptionDTO> availabilityDTOS = new ArrayList<>();
        for(StationException availability : stationAvailabilityBusiness.getAllAvailabilityByStation(stationId)){
            availabilityDTOS.add(stationAvailabilityMapper.toDto(availability));
        }
        return availabilityDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Secured("ROLE_USER")
    public StationExceptionDTO createAvailability(@RequestBody @Valid StationExceptionCreationDTO dto) {
        return stationAvailabilityMapper.toDto(
                stationAvailabilityBusiness.createStationAvailability(stationAvailabilityMapper.toEntity(dto), dto.getStationId())
        );
    }


    @PutMapping("/{id}")
    @Secured("ROLE_USER")
    public StationExceptionDTO updateAvailability(@PathVariable String id, @RequestBody StationExceptionCreationDTO dto) {
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
