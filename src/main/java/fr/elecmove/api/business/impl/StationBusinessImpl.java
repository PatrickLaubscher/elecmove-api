package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.StationBusiness;
import fr.elecmove.api.business.mapper.StationEntityMapper;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class StationBusinessImpl implements StationBusiness {


    private final StationRepository stationRepository;
    private final StationEntityMapper stationEntityMapper;


    public StationBusinessImpl(StationRepository stationRepository, StationEntityMapper stationEntityMapper) {
        this.stationRepository = stationRepository;
        this.stationEntityMapper = stationEntityMapper;
    }

    @Override
    public Station createStation(Station station, User user) {
        station.setUser(user);
        return stationRepository.save(station);
    }


    @Override
    public Station getStation(String id) {
        return stationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The station does not exist")
        );
    }


    @Override
    public List<Station> getAllStationByEmail(String email) {
        return stationRepository.findStationByUserEmail(email);
    }


    @Override
    public Station updateStation(String id, Station station, User user) {

        Station existingStation = stationRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The station does not exist")
        );
        if(!existingStation.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't modify this station.");
        }

        stationEntityMapper.merge(existingStation, station);

        return stationRepository.save(station);
    }


    @Override
    public void deleteStation(String id, User user) {

        Station existingStation = getStation(id);

        if(!existingStation.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this station.");
        }

        stationRepository.delete(existingStation);
    }


}
