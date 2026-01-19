package fr.elecmove.api;


import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiBookingTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    EntityManager em;

    @MockitoBean
    private MailService mailService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    User user1 = new User();
    String stationId;
    String carId;
    String bookingId;

    @BeforeEach
    void setUp() throws Exception {


        doNothing().when(mailService).sendEmailValidation(any(User.class), anyString());

        user1.setFirstname("firstname1");
        user1.setLastname("lastname1");
        user1.setEmail("firstname1@test.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRole("ROLE_USER");
        user1.setValidated(true);
        em.persist(user1);

        Car car = new Car();
        car.setUser(user1);
        car.setBrand("brand1");
        car.setRegistration("registration1");
        car.setType("type1");
        em.persist(car);
        carId = car.getId();

        LocationStation location = new LocationStation();
        location.setAddress("address1");
        location.setCity("city1");
        location.setZipcode("zipcode1");
        location.setLatitude(BigDecimal.valueOf(10.0));
        location.setLongitude(BigDecimal.valueOf(10.0));
        em.persist(location);

        Station station = new Station();
        station.setName("station1");
        station.setTarification(1.0);
        station.setType("type1");
        station.setPower("power1");
        station.setInstruction("instruction1");
        station.setFreeStanding(true);
        station.setAvailable(true);
        station.setLocation(location);
        station.setUser(user1);
        em.persist(station);
        stationId = station.getId();

        Booking booking1 = new Booking();
        booking1.setDate(LocalDate.of(2025, 1, 1));
        booking1.setStartTime(LocalTime.of(9, 0));
        booking1.setEndTime(LocalTime.of(11, 0));
        booking1.setUser(user1);
        booking1.setCar(car);
        booking1.setStation(station);
        em.persist(booking1);
        bookingId = booking1.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getWithIdShouldReturnBooking() throws Exception {
        mvc.perform(get("/api/bookings/"+bookingId).with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value("2025-01-01"))
                .andExpect(jsonPath("$.startTime").value("09:00"))
                .andExpect(jsonPath("$.endTime").value("11:00"));
    }

    @Test
    void getWithIdShouldThrow404IfNoBooking() throws Exception {
        mvc.perform(get("/api/bookings/dontexist").with(user(user1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void postShouldPersistBookingWithRightStationAndRightUserAndRightCar() throws Exception {

        mvc.perform(post("/api/bookings")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"date": "2025-01-01",
				"startTime": "09:00",
			    "endTime": "12:00",
			    "carId": "%s",
			    "stationId": "%s"
			}""".formatted(carId, stationId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.date").value("2025-01-01"))
                .andExpect(jsonPath("$.startTime").value("09:00"))
                .andExpect(jsonPath("$.endTime").value("12:00"))
                .andExpect(jsonPath("$.totalPrice").value(3.0))
                .andExpect(jsonPath("$.car.id").value(carId))
                .andExpect(jsonPath("$.station.id").value(stationId))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void putShouldUpdateBooking() throws Exception {
        mvc.perform(put("/api/bookings/"+bookingId)
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"endTime": "13:00"
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endTime").value("13:00"));
    }

    @Test
    void deleteShouldDeleteBookingById() throws Exception {
        mvc.perform(delete("/api/bookings/"+bookingId).with(user(user1)))
                .andExpect(status().isNoContent());
    }


}
