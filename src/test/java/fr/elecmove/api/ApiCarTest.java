package fr.elecmove.api;

import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.Role;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.Car;
import fr.elecmove.api.repository.RoleRepository;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiCarTest {

    @Autowired
    MockMvc mvc;
    @Autowired
    EntityManager em;

    @MockitoBean
    private MailService mailService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    User user1 = new User();
    String carId;

    @BeforeEach
    void setUp() throws Exception {

        Role roleUser = roleRepository.findByName("ROLE_USER").orElseGet(() -> {
            Role r = new Role();
            r.setName("ROLE_USER");
            return roleRepository.save(r);
        });

        doNothing().when(mailService).sendEmailValidation(any(User.class), anyString());

        user1.setFirstname("firstname1");
        user1.setLastname("lastname1");
        user1.setEmail("firstname1@test.com");
        user1.setPassword(passwordEncoder.encode("password"));
        user1.setRole(roleUser);
        user1.setValidated(true);
        em.persist(user1);

        Car car = new Car();
        car.setUser(user1);
        car.setBrand("brand1");
        car.setRegistration("registration1");
        car.setType("type1");
        em.persist(car);
        carId = car.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }



    @Test
    void getWithIdShouldReturnCar() throws Exception {
        mvc.perform(get("/api/cars/"+carId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("brand1"))
                .andExpect(jsonPath("$.registration").value("registration1"))
                .andExpect(jsonPath("$.type").value("type1"));
    }

    @Test
    void getWithIdShouldThrow404IfNoCar() throws Exception {
        mvc.perform(get("/api/cars/dontexist"))
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllShouldReturnCarList() throws Exception {
        mvc.perform(get("/api/cars").with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void postShouldPersistCarWithRightUser() throws Exception {

        mvc.perform(post("/api/cars")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"brand":"brand2",
				"registration":"registration2",
			    "type": "type2"
			}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.brand").value("brand2"))
                .andExpect(jsonPath("$.registration").value("registration2"))
                .andExpect(jsonPath("$.type").value("type2"))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void patchShouldUpdateCar() throws Exception {
        mvc.perform(patch("/api/cars/"+carId)
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"brand":"updated brand"
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.brand").value("updated brand"));
    }

    @Test
    void deleteShouldDeleteCarById() throws Exception {
        mvc.perform(delete("/api/cars/"+carId).with(user(user1)))
                .andExpect(status().isNoContent());
    }


}
