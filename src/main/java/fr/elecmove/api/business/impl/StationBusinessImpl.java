package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.StationBusiness;
import fr.elecmove.api.business.mapper.StationEntityMapper;
import fr.elecmove.api.model.LocationStation;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.BookingRepository;
import fr.elecmove.api.repository.StationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class StationBusinessImpl implements StationBusiness {


    private final StationRepository stationRepository;
    private final BookingRepository bookingRepository;
    private final StationEntityMapper stationEntityMapper;

    public StationBusinessImpl(StationRepository stationRepository, BookingRepository bookingRepository, StationEntityMapper stationEntityMapper) {
        this.stationRepository = stationRepository;
        this.bookingRepository = bookingRepository;
        this.stationEntityMapper = stationEntityMapper;
    }

    @Override
    public Station createStation(Station station, LocationStation location, User user) {
        station.setUser(user);
        station.setLocation(location);
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
        return stationRepository.findStationByUserEmailWithExceptions(email);
    }


    @Override
    public List<Station> getAllStationByLocation(double latitude, double longitude, double rayonMeters) {
        return stationRepository.findStationsNearby(latitude, longitude, rayonMeters);
    }

    @Override
    public List<Station> getNearbyAvailableStations(double latitude, double longitude, double rayonMeters, LocalDate date, LocalTime startTime, LocalTime endTime) {

        List<Station> nearbyStations = stationRepository.findStationsNearby(latitude, longitude, rayonMeters);

        String weekday = date.getDayOfWeek().name();
        Set<Station> stationsWithoutExceptions = new HashSet<>(
                stationRepository.findAvailableStationsByDayAndTime(weekday, startTime, endTime)
        );

        List<Station> withoutExceptions = nearbyStations.stream()
                .filter(stationsWithoutExceptions::contains)
                .toList();

        List<String> availableStationId = withoutExceptions.stream().map(Station::getId).toList();

        return stationRepository.filterStationsWithoutBooking(availableStationId, date, startTime, endTime);
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
