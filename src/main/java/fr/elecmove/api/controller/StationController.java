package fr.elecmove.api.controller;


import fr.elecmove.api.business.LocationStationBusiness;
import fr.elecmove.api.business.StationBusiness;
import fr.elecmove.api.controller.dto.CoordinatesWithRadiusDTO;
import fr.elecmove.api.controller.dto.CoordinatesWithRadiusAndTimeSlotsDTO;
import fr.elecmove.api.controller.dto.prebooking.PrebookingEstimateDTO;
import fr.elecmove.api.controller.dto.prebooking.PrebookingEstimateRequestDTO;
import fr.elecmove.api.controller.dto.station.StationCreationDTO;
import fr.elecmove.api.controller.dto.station.StationDTO;
import fr.elecmove.api.controller.dto.mapper.StationMapper;
import fr.elecmove.api.controller.dto.station.StationSingleDTO;
import fr.elecmove.api.model.LocationStation;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/stations")
public class StationController {

    StationBusiness stationBusiness;
    LocationStationBusiness locationStationBusiness;
    StationMapper stationMapper;

    public StationController(StationBusiness stationBusiness, LocationStationBusiness locationStationBusiness, StationMapper stationMapper) {
        this.stationBusiness = stationBusiness;
        this.locationStationBusiness = locationStationBusiness;
        this.stationMapper = stationMapper;
    }

    @GetMapping("/{id}")
    public StationDTO getStation(@PathVariable String id) {
        return stationMapper.toDto(stationBusiness.getStation(id));
    }


    @GetMapping
    public List<StationDTO> getAllStations(@AuthenticationPrincipal UserDetails userDetails) {
        List<StationDTO> stationDTOS = new ArrayList<>();
        for(Station station : stationBusiness.getAllStationByEmail(userDetails.getUsername())){
            stationDTOS.add(stationMapper.toDto(station));
        }
        return stationDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StationDTO createStation(@RequestBody @Valid StationCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        LocationStation location = locationStationBusiness.getLocation(dto.getLocationStationId());
        return stationMapper.toDto(
                stationBusiness.createStation(stationMapper.toEntity(dto), location, user)
        );
    }


    @PutMapping("/{id}")
    public StationDTO updateStation(@PathVariable String id, @RequestBody StationCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return stationMapper.toDto(
                stationBusiness.updateStation(id, stationMapper.toEntity(dto), user)
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStation(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        stationBusiness.deleteStation(id, user);
    }


    @PostMapping("/nearby")
    public List<StationSingleDTO> getAllStationsByLocation(@RequestBody @Valid CoordinatesWithRadiusDTO dto) {
        List<StationSingleDTO> stationDTOS = new ArrayList<>();
        for(Station station : stationBusiness.getAllStationByLocation(dto.getLatitude(), dto.getLongitude(), dto.getRayonMeters())){
            stationDTOS.add(stationMapper.toSingleDto(station));
        }
        return stationDTOS;
    }

    @PostMapping("/nearby-available")
    public List<StationSingleDTO> getAllNearbyStationWithAvailability(@RequestBody @Valid CoordinatesWithRadiusAndTimeSlotsDTO dto) {

        Map<Station, Boolean> stationsMap = stationBusiness.getNearbyAvailableStations(
                dto.getLatitude(), dto.getLongitude(), dto.getRayonMeters(),
                dto.getDate(), dto.getStartTime(), dto.getEndTime()
        );

        return stationsMap.entrySet().stream()
                .map(entry -> stationMapper.toSingleDtoWithAvailability(
                        entry.getKey(),
                        entry.getValue()
                ))
                .toList();

    }

    @PostMapping("/{id}/prebooking/estimate")
    public PrebookingEstimateDTO getPrebookingEstimate(@PathVariable String id, @RequestBody @Valid PrebookingEstimateRequestDTO dto) {

        if (dto.getBookingStartTime() == null || dto.getBookingEndTime() == null) {
            throw new IllegalArgumentException("Start time and end time must not be null");
        }
        if (dto.getBookingEndTime().isBefore(dto.getBookingStartTime())) {
            throw new IllegalArgumentException("End time must be after start time");
        }

        Double bookingEstimatePrice = stationBusiness.bookingEstimatePrice(id, dto.getBookingStartTime(), dto.getBookingEndTime());
        Duration bookingDuration = Duration.between(dto.getBookingStartTime(), dto.getBookingEndTime());

        return new PrebookingEstimateDTO(bookingDuration, bookingEstimatePrice);
    }



}
