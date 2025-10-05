package fr.elecmove.api;


import fr.elecmove.api.messaging.MailService;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.UserAddress;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Transactional
class ApiUserAddressTest {

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
    String userAddressId;

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

        UserAddress address = new UserAddress();
        address.setUser(user1);
        address.setAddressName("address1");
        address.setAddress("address1");
        address.setCity("city1");
        address.setZipcode("zipcode1");
        address.setLatitude(10.0);
        address.setLongitude(10.0);
        em.persist(address);
        userAddressId = address.getId();

        em.flush();
    }

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }



    @Test
    void getWithIdShouldReturnUserAddress() throws Exception {
        mvc.perform(get("/api/user-addresses/"+userAddressId).with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.addressName").value("address1"))
                .andExpect(jsonPath("$.address").value("address1"))
                .andExpect(jsonPath("$.city").value("city1"))
                .andExpect(jsonPath("$.zipcode").value("zipcode1"))
                .andExpect(jsonPath("$.latitude").value(10.0))
                .andExpect(jsonPath("$.longitude").value(10.0));
    }

    @Test
    void getWithIdShouldThrow404IfNoUserAddress() throws Exception {
        mvc.perform(get("/api/user-addresses/dontexist").with(user(user1)))
                .andExpect(status().isNotFound());
    }


    @Test
    void getAllShouldReturnUserAddressList() throws Exception {
        mvc.perform(get("/api/user-addresses").with(user(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }


    @Test
    void postShouldPersistUserAddressWithRightUser() throws Exception {

        mvc.perform(post("/api/user-addresses")
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"addressName":"adresse1",
				"address":"4 rue de l'adresse",
			    "city": "ville",
			    "zipcode": "12345",
			    "latitude": 10.00,
				"longitude": 10.00
			}"""))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.addressName").value("adresse1"))
                .andExpect(jsonPath("$.address").value("4 rue de l'adresse"))
                .andExpect(jsonPath("$.city").value("ville"))
                .andExpect(jsonPath("$.zipcode").value("12345"))
                .andExpect(jsonPath("$.latitude").value(10.00))
                .andExpect(jsonPath("$.longitude").value(10.00))
                .andExpect(jsonPath("$.user.email").value(user1.getEmail()));
    }

    @Test
    void putShouldUpdateUserAddress() throws Exception {
        mvc.perform(put("/api/user-addresses/"+userAddressId)
                        .with(user(user1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
			{
				"address":"updated address",
			    "latitude": 15.00,
				"longitude": 15.00
			}"""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("updated address"))
                .andExpect(jsonPath("$.latitude").value(15.00))
                .andExpect(jsonPath("$.longitude").value(15.00));
    }

    @Test
    void deleteShouldDeleteUserAddressById() throws Exception {
        mvc.perform(delete("/api/user-addresses/"+userAddressId).with(user(user1)))
                .andExpect(status().isNoContent());
    }


}
