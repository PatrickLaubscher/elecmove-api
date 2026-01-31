package fr.elecmove.api.setup;


import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final BookingStatusRepository statusRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final LocationStationRepository locationStationRepository;
    private final PasswordEncoder passwordEncoder;
    private final CarRepository carRepository;
    private final BookingRepository bookingRepository;
    private final StationExceptionRepository stationExceptionRepository;
    private final FavoriteStationRepository favoriteStationRepository;
    private final ReviewRepository reviewRepository;

    public DataInitializer(BookingStatusRepository statusRepository, UserRepository userRepository, StationRepository stationRepository, LocationStationRepository locationStationRepository, PasswordEncoder passwordEncoder, CarRepository carRepository, BookingRepository bookingRepository, StationExceptionRepository stationExceptionRepository, FavoriteStationRepository favoriteStationRepository, ReviewRepository reviewRepository) {
        this.statusRepository = statusRepository;
        this.userRepository = userRepository;
        this.stationRepository = stationRepository;
        this.locationStationRepository = locationStationRepository;
        this.passwordEncoder = passwordEncoder;
        this.carRepository = carRepository;
        this.bookingRepository = bookingRepository;
        this.stationExceptionRepository = stationExceptionRepository;
        this.favoriteStationRepository = favoriteStationRepository;
        this.reviewRepository = reviewRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        BookingStatus status1;
        BookingStatus status2;
        BookingStatus status3;
        BookingStatus status4;
        BookingStatus status5;
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

            status4 = new BookingStatus();
            status4.setName("Refusé");

            status5 = new BookingStatus();
            status5.setName("Annulé");

            statusRepository.save(status1);
            statusRepository.save(status2);
            statusRepository.save(status3);
            statusRepository.save(status4);
            statusRepository.save(status5);

        } else {
            status1 = statusRepository.findByName("En attente").orElse(statusRepository.findAll().get(0));
            status2 = statusRepository.findByName("Confimé").orElse(statusRepository.findAll().get(1));
            status3 = statusRepository.findByName("Payé").orElse(statusRepository.findAll().get(2));
            status4 = statusRepository.findByName("Refusé").orElse(statusRepository.findAll().get(2));
            status5 = statusRepository.findByName("Annulé").orElse(statusRepository.findAll().get(2));
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


        // === 5. Supprimer et recréer les stations de Lyon uniquement si elles existent déjà
        // Supprimer seulement les stations "Borne" créées par ce DataInitializer
        if (stationRepository.count() > 0) {
            var existingStations = stationRepository.findAll();
            var bornesLyon = existingStations.stream()
                    .filter(s -> s.getName() != null && s.getName().startsWith("Borne "))
                    .toList();
            if (!bornesLyon.isEmpty()) {
                // Supprimer d'abord les bookings et favoris associés à ces stations
                for (Station station : bornesLyon) {
                    var bookings = bookingRepository.findByStationId(station.getId());
                    if (!bookings.isEmpty()) {
                        bookingRepository.deleteAll(bookings);
                    }
                    var favorites = favoriteStationRepository.findAllByStationId(station.getId());
                    if (!favorites.isEmpty()) {
                        favoriteStationRepository.deleteAll(favorites);
                    }
                }
                // Maintenant on peut supprimer les stations
                stationRepository.deleteAll(bornesLyon);
            }
        }

        // Créer les 23 stations avec coordonnées GPS précises et adresses réelles
        station1 = createStation("Borne Bellecour", 45.7578, 4.8320,
            "Place Bellecour", "Lyon", "69002", "7,4 kVA", 8.0, true, true, user1);
        station2 = createStation("Borne Part-Dieu", 45.7603, 4.8575,
            "Rue de la Villette", "Lyon", "69003", "22 kVA", 15.0, false, true, user2);
        createStation("Borne Confluence", 45.7410, 4.8150,
            "Cours Charlemagne", "Lyon", "69002", "11 kVA", 12.0, true, true, user2);

        // Stations avec coordonnées GPS et adresses réelles de Lyon
        createStation("Borne Hôtel de Ville", 45.7675, 4.8348, "1 Place de la Comédie", "Lyon", "69001", "11 kVA", 12.0, false, true, user1);
        createStation("Borne Croix-Rousse", 45.7743, 4.8322, "Place de la Croix-Rousse", "Lyon", "69004", "22 kVA", 15.0, true, true, user1);
        createStation("Borne Terreaux", 45.7676, 4.8334, "Place des Terreaux", "Lyon", "69001", "7,4 kVA", 8.0, false, true, user2);
        createStation("Borne Vieux Lyon", 45.7631, 4.8265, "Rue Saint-Jean", "Lyon", "69005", "3,7 kVA", 5.0, true, true, user1);
        createStation("Borne Fourvière", 45.7625, 4.8227, "Place de Fourvière", "Lyon", "69005", "11 kVA", 12.0, false, true, user2);
        createStation("Borne Perrache", 45.7490, 4.8258, "Cours de Verdun", "Lyon", "69002", "22 kVA", 15.0, true, true, user1);
        createStation("Borne Guillotière", 45.7538, 4.8422, "Cours Gambetta", "Lyon", "69007", "7,4 kVA", 8.0, false, true, user2);
        createStation("Borne Jean Macé", 45.7463, 4.8423, "Place Jean Macé", "Lyon", "69007", "11 kVA", 12.0, true, true, user1);
        createStation("Borne Gerland", 45.7268, 4.8273, "Avenue Tony Garnier", "Lyon", "69007", "22 kVA", 15.0, false, true, user2);
        createStation("Borne Monplaisir", 45.7515, 4.8713, "Rue Marius Berliet", "Lyon", "69008", "3,7 kVA", 5.0, true, true, user1);
        createStation("Borne Sans Souci", 45.7549, 4.8815, "Avenue Lacassagne", "Lyon", "69003", "7,4 kVA", 8.0, false, true, user2);
        createStation("Borne Montchat", 45.7591, 4.8838, "Place Ronde", "Lyon", "69003", "11 kVA", 12.0, true, true, user1);
        createStation("Borne Vaise", 45.7805, 4.8032, "Rue de Bourgogne", "Lyon", "69009", "22 kVA", 15.0, false, true, user2);
        createStation("Borne Gorge de Loup", 45.7663, 4.8048, "Avenue du Général Eisenhower", "Lyon", "69009", "7,4 kVA", 8.0, true, true, user1);
        createStation("Borne Saxe-Gambetta", 45.7537, 4.8453, "Cours Gambetta", "Lyon", "69003", "11 kVA", 12.0, false, true, user2);
        createStation("Borne Brotteaux", 45.7695, 4.8573, "Boulevard des Brotteaux", "Lyon", "69006", "22 kVA", 15.0, true, true, user1);
        createStation("Borne Foch", 45.7695, 4.8468, "Avenue Maréchal Foch", "Lyon", "69006", "3,7 kVA", 5.0, false, true, user2);
        createStation("Borne Masséna", 45.7693, 4.8503, "Cours Vitton", "Lyon", "69006", "7,4 kVA", 8.0, true, true, user1);
        createStation("Borne Tête d'Or", 45.7752, 4.8545, "Boulevard des Belges", "Lyon", "69006", "11 kVA", 12.0, false, true, user2);
        createStation("Borne Garibaldi", 45.7568, 4.8523, "Rue Garibaldi", "Lyon", "69006", "22 kVA", 15.0, true, true, user1);


        // === 6. Add station exceptions


        // === 7. Add Bookings
        if (bookingRepository.count() == 0) {
            // Bookings passés pour user1 (Alice)
            bookingRepository.save(new Booking(
                LocalDate.now().minusMonths(2),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                45.0, user1, car1, station1, status3)); // Payé

            bookingRepository.save(new Booking(
                LocalDate.now().minusMonths(1),
                LocalTime.of(14, 0),
                LocalTime.of(18, 0),
                60.0, user1, car1, station2, status3)); // Payé

            bookingRepository.save(new Booking(
                LocalDate.now().minusWeeks(2),
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                45.0, user1, car1, station1, status3)); // Payé

            bookingRepository.save(new Booking(
                LocalDate.now().minusDays(5),
                LocalTime.of(16, 0),
                LocalTime.of(19, 0),
                45.0, user1, car1, station2, status3)); // Payé

            // Bookings passés pour user2 (Robert)
            bookingRepository.save(new Booking(
                LocalDate.now().minusMonths(3),
                LocalTime.of(8, 0),
                LocalTime.of(11, 0),
                45.0, user2, car2, station2, status3)); // Payé

            bookingRepository.save(new Booking(
                LocalDate.now().minusWeeks(3),
                LocalTime.of(15, 0),
                LocalTime.of(17, 0),
                30.0, user2, car2, station1, status3)); // Payé

            bookingRepository.save(new Booking(
                LocalDate.now().minusDays(10),
                LocalTime.of(11, 0),
                LocalTime.of(14, 0),
                45.0, user2, car2, station2, status5)); // Annulé

            // Bookings futurs pour user1 (Alice)
            bookingRepository.save(new Booking(
                LocalDate.now().plusDays(2),
                LocalTime.of(9, 0),
                LocalTime.of(12, 0),
                45.0, user1, car1, station1, status2)); // Confirmé

            bookingRepository.save(new Booking(
                LocalDate.now().plusWeeks(1),
                LocalTime.of(14, 0),
                LocalTime.of(17, 0),
                45.0, user1, car1, station2, status1)); // En attente

            bookingRepository.save(new Booking(
                LocalDate.now().plusWeeks(2),
                LocalTime.of(10, 0),
                LocalTime.of(13, 0),
                45.0, user1, car1, station1, status1)); // En attente

            // Bookings futurs pour user2 (Robert)
            bookingRepository.save(new Booking(
                LocalDate.now().plusDays(3),
                LocalTime.of(16, 0),
                LocalTime.of(19, 0),
                45.0, user2, car2, station2, status2)); // Confirmé

            bookingRepository.save(new Booking(
                LocalDate.now().plusWeeks(1),
                LocalTime.of(8, 0),
                LocalTime.of(11, 0),
                45.0, user2, car2, station1, status1)); // En attente

            // Booking du jour pour user2
            bookingRepository.save(new Booking(
                LocalDate.now(),
                LocalTime.of(14, 0),
                LocalTime.of(16, 0),
                40.0, user2, car2, station1, status1)); // En attente
        }

        // === 8. Reviews
        if (reviewRepository.count() == 0) {
            Review review1 = new Review();
            review1.setComment("Super application, très pratique pour trouver des bornes !");
            review1.setRate(5);
            review1.setUser(user1);
            reviewRepository.save(review1);

            Review review2 = new Review();
            review2.setComment("Bon service, quelques améliorations possibles sur l'interface.");
            review2.setRate(4);
            review2.setUser(user2);
            reviewRepository.save(review2);

            Review review3 = new Review();
            review3.setComment("Facile à utiliser, je recommande !");
            review3.setRate(5);
            review3.setUser(user1);
            reviewRepository.save(review3);
        }

    }

    private Station createStation(String name, double latitude, double longitude, String address, String city, String zipcode, String power, Double tarification, Boolean freeStanding, boolean available, User user) {
        LocationStation location = new LocationStation(
                BigDecimal.valueOf(latitude).setScale(6, RoundingMode.HALF_UP),
                BigDecimal.valueOf(longitude).setScale(6, RoundingMode.HALF_UP));

        if (address != null) location.setAddress(address);
        if (city != null) location.setCity(city);
        if (zipcode != null) location.setZipcode(zipcode);

        locationStationRepository.save(location);

        Station station = new Station();
        station.setName(name);
        station.setAvailable(available);
        station.setLocation(location);
        station.setUser(user);
        if (power != null) station.setPower(power);
        if (tarification != null) station.setTarification(tarification);
        if (freeStanding != null) station.setFreeStanding(freeStanding);

        return stationRepository.save(station);
    }

}