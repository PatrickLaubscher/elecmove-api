package fr.elecmove.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import api.config.JwtProperties;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
public class ApiElecMove {
    public static void main(String[] args) {
        SpringApplication.run(ApiElecMove.class, args);
    }
}
