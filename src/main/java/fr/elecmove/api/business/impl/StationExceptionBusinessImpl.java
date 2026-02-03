package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.StationExceptionBusiness;
import fr.elecmove.api.business.mapper.StationExceptionEntityMapper;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.StationException;
import fr.elecmove.api.repository.StationExceptionRepository;
import fr.elecmove.api.repository.StationRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class StationExceptionBusinessImpl implements StationExceptionBusiness {

    private static final Map<String, DayOfWeek> FRENCH_TO_DAY_OF_WEEK = Map.of(
            "lundi", DayOfWeek.MONDAY,
            "mardi", DayOfWeek.TUESDAY,
            "mercredi", DayOfWeek.WEDNESDAY,
            "jeudi", DayOfWeek.THURSDAY,
            "vendredi", DayOfWeek.FRIDAY,
            "samedi", DayOfWeek.SATURDAY,
            "dimanche", DayOfWeek.SUNDAY
    );

    private String convertDayToEnglish(String frenchDay) {
        DayOfWeek dayOfWeek = FRENCH_TO_DAY_OF_WEEK.get(frenchDay.toLowerCase());
        if (dayOfWeek == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid day: " + frenchDay);
        }
        return dayOfWeek.name();
    }

    StationExceptionRepository availabilityRepository;
    StationRepository stationRepository;
    StationExceptionEntityMapper stationExceptionEntityMapper;

    public StationExceptionBusinessImpl(StationExceptionRepository availabilityRepository, StationRepository stationRepository, StationExceptionEntityMapper stationExceptionEntityMapper) {
        this.availabilityRepository = availabilityRepository;
        this.stationRepository = stationRepository;
        this.stationExceptionEntityMapper = stationExceptionEntityMapper;
    }

    @Override
    public StationException createStationAvailability(StationException availability, String stationId) {
        Station station = stationRepository.findById(stationId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The station does not exist")
        );
        availability.setStation(station);
        availability.setDay(convertDayToEnglish(availability.getDay()));
        return availabilityRepository.save(availability);
    }

    @Override
    public StationException getStationAvailability(String id) {
        return availabilityRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The availability does not exist")
        );
    }

    @Override
    public List<StationException> getAllAvailabilityByStation(String stationId) {
        return availabilityRepository.findByStationId(stationId);
    }

    @Override
    public StationException updateStationAvailability(StationException availability, String availabilityId) {

        StationException existingAvailability = availabilityRepository.findById(availabilityId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The availability does not exist")
        );

        if (availability.getDay() != null) {
            availability.setDay(convertDayToEnglish(availability.getDay()));
        }
        stationExceptionEntityMapper.merge(existingAvailability, availability);
        return availabilityRepository.save(existingAvailability);
    }

    @Override
    public void deleteStationAvailability(String id) {
        availabilityRepository.deleteById(id);
    }

}
