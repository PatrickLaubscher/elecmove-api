package fr.elecmove.api;

import fr.elecmove.api.model.LocationStation;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.repository.LocationStationRepository;
import fr.elecmove.api.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test-mysql")
@DisabledIfEnvironmentVariable(named = "CI", matches = "true", disabledReason = "Docker not available on GitLab shared runners")
class StationRepositoryTest {

    @Container
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysql::getJdbcUrl);
        registry.add("spring.datasource.username", mysql::getUsername);
        registry.add("spring.datasource.password", mysql::getPassword);
        registry.add("spring.datasource.driver-class-name", mysql::getDriverClassName);
    }

    @Autowired
    StationRepository stationRepository;

    @Autowired
    LocationStationRepository locationRepository;


    @Test
    void shouldFindStationsNearby() {

        LocationStation loc1 = new LocationStation(BigDecimal.valueOf(45.7578), BigDecimal.valueOf(4.8320)); // Bellecour
        LocationStation loc2 = new LocationStation(BigDecimal.valueOf(45.7600), BigDecimal.valueOf(4.8610)); // Part-Dieu
        locationRepository.saveAll(List.of(loc1, loc2));

        Station s1 = new Station();
        s1.setName("Borne Bellecour");
        s1.setLocation(loc1);
        s1.setAvailable(true);
        Station s2 = new Station();
        s2.setName("Borne Part Dieu");
        s2.setLocation(loc2);
        s2.setAvailable(true);
        stationRepository.saveAll(List.of(s1, s2));

        List<Station> results = stationRepository.findStationsNearby(45.7577, 4.8321, 3000);

        assertThat(results)
                .isNotEmpty()
                .allMatch(Station::getAvailable)
                .extracting(Station::getName)
                .contains("Borne Bellecour");
    }

}
