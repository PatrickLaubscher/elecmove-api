package fr.elecmove.api;

import fr.elecmove.api.config.JwtConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtConfig.class)
public class ApiElecMove {
    public static void main(String[] args) {
        SpringApplication.run(ApiElecMove.class, args);
    }
}
