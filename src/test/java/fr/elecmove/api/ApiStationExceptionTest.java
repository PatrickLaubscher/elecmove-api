package fr.elecmove.api;


import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.StationException;
import fr.elecmove.api.model.User;
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

import java.time.LocalTime;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;



@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiStationExceptionTest {


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
    String availabilityId;

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

        Station station1 = new Station();
        station1.setName("station1");
        station1.setUser(user1);
        em.persist(station1);
        stationId = station1.getId();

        StationException availability = new StationException();
        availability.setDay("monday");
        availability.setStartLocalTime(LocalTime.of(9, 0));
        availability.setEndLocalTime(LocalTime.of(12, 0));
        availability.setStation(station1);
        em.persist(availability);
        availabilityId = availability.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }


    @Test
    void getWithIdShouldReturnAvailability() throws Exception {
        mvc.perform(get("/api/availabilities/"+availabilityId).with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.day").value("monday"))
                .andExpect(jsonPath("$.startLocalTime").value("09:00"))
                .andExpect(jsonPath("$.endLocalTime").value("12:00"))
                .andExpect(jsonPath("$.station.id").value(stationId));
    }

    @Test
    void getWithIdShouldThrow404IfNoAvailability() throws Exception {
        mvc.perform(get("/api/availabilities/dontexist").with(user(user1)))
                .andExpect(status().isNotFound());
    }

/*
    @Test
    void getAllShouldReturnAvailabilitiesList() throws Exception {
        mvc.perform(get("/api/availabilities/")
                    .param("stationId", stationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }
*/


    @Test
    void postShouldPersistAvailabilityWithRightStation() throws Exception {

        mvc.perform(post("/api/availabilities")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"day":"Lundi",
				"startLocalTime":"09:00",
			    "endLocalTime": "12:00",
			    "stationId": "%s"
			}""".formatted(stationId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.day").value("MONDAY"))
                .andExpect(jsonPath("$.startLocalTime").value("09:00"))
                .andExpect(jsonPath("$.endLocalTime").value("12:00"))
                .andExpect(jsonPath("$.station.id").value(stationId));
    }

    @Test
    void putShouldUpdateAvailability() throws Exception {
        mvc.perform(put("/api/availabilities/"+availabilityId)
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"startLocalTime":"10:00"
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startLocalTime").value("10:00"));
    }

    @Test
    void deleteShouldDeleteAvailabilityById() throws Exception {
        mvc.perform(delete("/api/availabilities/"+availabilityId).with(user(user1)))
                .andExpect(status().isNoContent());
    }



}
