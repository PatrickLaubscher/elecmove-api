package fr.elecmove.api.setup;


import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookingStatusRepository statusRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final LocationStationRepository locationStationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;

    public DataInitializer(BookingStatusRepository statusRepository, UserRepository userRepository, StationRepository stationRepository, LocationStationRepository locationStationRepository, PasswordEncoder passwordEncoder, CarRepository carRepository, BookingRepository bookingRepository) {
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.stationRepository = stationRepository;
        this.locationStationRepository = locationStationRepository;
        this.passwordEncoder = passwordEncoder;
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        BookingStatus status1;
        BookingStatus status2;
        BookingStatus status3;
        User user1;
        User user2;
        Car car1;
        Car car2;
        Station station1;
        Station station2;
        Booking booking1;




        // === 2. Booking status
        if  (statusRepository.count() == 0) {
            status1 = new BookingStatus();
            status1.setName("En attente");

            status2 = new BookingStatus();
            status2.setName("Confirmé");

            status3 = new BookingStatus();
            status3.setName("Payé");

            statusRepository.save(status1);
            statusRepository.save(status2);
            statusRepository.save(status3);

        } else {
            status1 = statusRepository.findByName("En attente").orElse(statusRepository.findAll().get(0));
            status2 = statusRepository.findByName("Confimé").orElse(statusRepository.findAll().get(1));
            status3 = statusRepository.findByName("Payé").orElse(statusRepository.findAll().get(2));
        }

        // === 3. Users
        if (userRepository.count() == 0) {
            user1 = userRepository.save(new User("Alice", "Durand", "0000000", "alice@test.com",
                    "ROLE_USER", passwordEncoder.encode("password"), true));

            user2 = userRepository.save(new User("Robert", "Martin", "0000000", "robert@test.com",
                    "ROLE_USER", passwordEncoder.encode("password"), true));
        } else {
            user1 = userRepository.findByEmail("alice@test.com")
                    .orElse(userRepository.findAll().get(0));
            user2 = userRepository.findByEmail("robert@test.com")
                    .orElse(userRepository.findAll().get(0));
        }

        // === 4. Cars
        if (carRepository.count() == 0) {
            car1 = carRepository.save(new Car("Mégane","0000000", "Renault", user1));
            car2 = carRepository.save(new Car("308","0000000", "Peugeot", user2));
        } else {
            car1 = carRepository.findCarByUserEmail("alice@test.com").get(0);
            car2 = carRepository.findCarByUserEmail("robert@test.com").get(0);
        }


        // === 5. Add Stations at Lyon
        if (stationRepository.count() == 0) {

            station1 = createStation("Borne Bellecour", 45.7578, 4.8320, true, user1);
            station2 = createStation("Borne Part-Dieu", 45.7600, 4.8610, true, user2);
            createStation("Borne Confluence", 45.7410, 4.8150, true, user2);

            Random random = new Random();

            // Rectangle englobant Lyon (approximatif)
            double minLat = 45.72;   // sud (Gerland)
            double maxLat = 45.80;   // nord (Croix-Rousse)
            double minLon = 4.80;    // ouest (Vaise / Fourvière)
            double maxLon = 4.90;    // est (Part-Dieu / Montchat)

            for (int i = 1; i <= 20; i++) {
                double latitude = minLat + (maxLat - minLat) * random.nextDouble();
                double longitude = minLon + (maxLon - minLon) * random.nextDouble();

                boolean available = random.nextBoolean();

                createStation("Borne Lyon " + i, latitude, longitude, available, user1);
            }

        } else {
            station1 = stationRepository.findStationByUserEmail("alice@test.com").get(0);
        }

        // === 6. Availabilities


        // === 7. Add Bookings
        if (bookingRepository.count() == 0) {
            bookingRepository.save(new Booking(LocalDate.now(), LocalTime.of(14,0), LocalTime.of(16, 0), 40.0, user2, car2, station1, status1));
        }



    }

    private Station createStation(String name, double latitude, double longitude, boolean available, User user) {
        LocationStation location = new LocationStation(latitude, longitude);
        locationStationRepository.save(location);

        Station station = new Station();
        station.setName(name);
        station.setAvailable(available);
        station.setLocation(location);
        station.setUser(user);

        return stationRepository.save(station);
    }


}