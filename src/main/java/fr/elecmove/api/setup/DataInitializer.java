package fr.elecmove.api.setup;


import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final BookingStatusRepository statusRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final LocationStationRepository locationStationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CarRepository carRepository;

    public DataInitializer(RoleRepository roleRepository,
                           BookingStatusRepository statusRepository,
                           UserRepository userRepository,
                           StationRepository stationRepository,
                           LocationStationRepository locationStationRepository,
                           PasswordEncoder passwordEncoder, CarRepository carRepository) {
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.stationRepository = stationRepository;
        this.locationStationRepository = locationStationRepository;
        this.passwordEncoder = passwordEncoder;
        this.carRepository = carRepository;
    }


    @Override
    public void run(String... args) throws Exception {

        User user1;

        // === 1. create Roles
        if (roleRepository.count() == 0) {
            Role adminRole = new Role();
            adminRole.setName("ROLE_ADMIN");

            Role userRole = new Role();
            userRole.setName("ROLE_USER");

            roleRepository.save(adminRole);
            roleRepository.save(userRole);

        }

        Role userRole = roleRepository.findByName("ROLE_USER").orElseThrow();

        // === 2. create Booking status
        if  (statusRepository.count() == 0) {
            BookingStatus status1 = new BookingStatus();
            status1.setName("En attente");

            BookingStatus status2 = new BookingStatus();
            status2.setName("Confirmé");

            BookingStatus status3 = new BookingStatus();
            status3.setName("Payé");

            statusRepository.save(status1);
            statusRepository.save(status2);
            statusRepository.save(status3);

        }

        // === 3. create Users
        if (userRepository.count() == 0) {
            user1 = userRepository.save(new User("Alice", "Durand", "0000000", "alice@test.com",
                    userRole, passwordEncoder.encode("password"), true));

            userRepository.save(new User("Bob", "Martin", "0000000", "bob@test.com",
                    userRole, passwordEncoder.encode("password"), true));
        } else {
            user1 = userRepository.findByEmail("alice@test.com")
                    .orElse(userRepository.findAll().get(0)); // fallback : prends n'importe quel user
        }

        // === 4. create car
        if (carRepository.count() == 0) {
            carRepository.save(new Car("Mégane","0000000", "Renault", user1));
        }


        // === 5. Add Stations at Lyon
        if (stationRepository.count() == 0) {

            createStation("Borne Bellecour", 45.7578, 4.8320, true);
            createStation("Borne Part-Dieu", 45.7600, 4.8610, true);
            createStation("Borne Confluence", 45.7410, 4.8150, true);

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

                createStation("Borne Lyon " + i, latitude, longitude, available);
            }

        }

    }


    private void createStation(String name, double latitude, double longitude, boolean available) {
        LocationStation location = new LocationStation(latitude, longitude);
        locationStationRepository.save(location);

        Station station = new Station();
        station.setName(name);
        station.setAvailable(available);
        station.setLocation(location);

        stationRepository.save(station);
    }


}