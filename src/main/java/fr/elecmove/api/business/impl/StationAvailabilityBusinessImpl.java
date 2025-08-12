package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.StationAvailabilityBusiness;
import fr.elecmove.api.business.mapper.StationAvailabilityEntityMapper;
import fr.elecmove.api.model.StationAvailability;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.repository.StationAvailabilityRepository;
import fr.elecmove.api.repository.StationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class StationAvailabilityBusinessImpl implements StationAvailabilityBusiness {

    StationAvailabilityRepository availabilityRepository;
    StationRepository stationRepository;
    StationAvailabilityEntityMapper stationAvailabilityEntityMapper;

    public StationAvailabilityBusinessImpl(StationAvailabilityRepository availabilityRepository, StationRepository stationRepository, StationAvailabilityEntityMapper stationAvailabilityEntityMapper) {
        this.availabilityRepository = availabilityRepository;
        this.stationRepository = stationRepository;
        this.stationAvailabilityEntityMapper = stationAvailabilityEntityMapper;
    }

    @Override
    public StationAvailability createStationAvailability(StationAvailability availability, String stationId) {

        Station station = stationRepository.findById(stationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The station does not exist")
        );
        availability.setStation(station);
        return availabilityRepository.save(availability);
    }

    @Override
    public StationAvailability getStationAvailability(String id) {
        return availabilityRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The availability does not exist")
        );
    }

    @Override
    public List<StationAvailability> getAllAvailabilityByStation(String stationId) {
        return availabilityRepository.findByStationId(stationId);
    }

    @Override
    public StationAvailability updateStationAvailability(StationAvailability availability, String availabilityId) {

        StationAvailability existingAvailability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The availability does not exist")
        );

        stationAvailabilityEntityMapper.merge(existingAvailability, availability);
        return availabilityRepository.save(availability);
    }

    @Override
    public void deleteStationAvailability(String id) {
        availabilityRepository.deleteById(id);
    }

}
