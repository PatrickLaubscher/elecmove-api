package fr.elecmove.api.repository;


import fr.elecmove.api.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, String> {

    List<Car> findCarByUserEmail(String email);

}
