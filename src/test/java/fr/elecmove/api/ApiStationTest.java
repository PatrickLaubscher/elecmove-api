package fr.elecmove.api;

import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.hamcrest.Matchers;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiStationTest {

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
    LocationStation location = new LocationStation();
    String locationId;
    String stationId;

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

        location.setAddress("address1");
        location.setCity("city1");
        location.setZipcode("zipcode1");
        location.setLatitude(10.0);
        location.setLongitude(10.0);
        em.persist(location);
        locationId = location.getId();

        Station station = new Station();
        station.setName("station1");
        station.setTarification(1.0);
        station.setType("type1");
        station.setPower("power1");
        station.setInstruction("instruction1");
        station.setFreeStanding(true);
        station.setLocation(location);
        station.setUser(user1);
        em.persist(station);
        stationId = station.getId();


        LocationStation loc1 = new LocationStation(45.7578, 4.8320); // Bellecour
        LocationStation loc2 = new LocationStation(45.7600, 4.8610); // Part-Dieu
        em.persist(loc1);
        em.persist(loc2);

        Station s1 = new Station();
        s1.setName("Borne Bellecour");
        s1.setLocation(loc1);
        s1.setAvailable(true);
        Station s2 = new Station();
        s2.setName("Borne Part Dieu");
        s2.setLocation(loc2);
        s2.setAvailable(true);
        em.persist(s1);
        em.persist(s2);


        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getWithIdShouldReturnLocationStation() throws Exception {
        mvc.perform(get("/api/stations/"+stationId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("station1"))
                .andExpect(jsonPath("$.tarification").value(1.0))
                .andExpect(jsonPath("$.type").value("type1"))
                .andExpect(jsonPath("$.power").value("power1"))
                .andExpect(jsonPath("$.instruction").value("instruction1"))
                .andExpect(jsonPath("$.freeStanding").value(true))
                .andExpect(jsonPath("$.location.address").value(location.getAddress()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void getWithIdShouldThrow404IfNoLocationStation() throws Exception {
        mvc.perform(get("/api/locations/dontexist"))
                .andExpect(status().isNotFound());
    }


    @Test
    void postShouldPersistStationWithRightUserAndRightLocation() throws Exception {

        mvc.perform(post("/api/stations")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"name": "station2",
				"tarification": 1.5,
			    "type": "type2",
			    "power": "power2",
			    "instruction": "instruction2",
				"freeStanding": false,
				"available": true,
				"locationStationId": "%s"
			}""".formatted(locationId)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("station2"))
                .andExpect(jsonPath("$.tarification").value(1.5))
                .andExpect(jsonPath("$.type").value("type2"))
                .andExpect(jsonPath("$.power").value("power2"))
                .andExpect(jsonPath("$.instruction").value("instruction2"))
                .andExpect(jsonPath("$.freeStanding").value(false))
                .andExpect(jsonPath("$.location.address").value(location.getAddress()))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void putShouldUpdateUserAddress() throws Exception {
        mvc.perform(put("/api/stations/"+stationId)
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"name": "updated station1",
				"tarification": 1.5,
			    "available": false
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("updated station1"))
                .andExpect(jsonPath("$.tarification").value(1.5))
                .andExpect(jsonPath("$.available").value(false));
    }

    @Test
    void deleteShouldDeleteUserAddressById() throws Exception {
        mvc.perform(delete("/api/stations/"+stationId).with(user(user1)))
                .andExpect(status().isNoContent());
    }



}
