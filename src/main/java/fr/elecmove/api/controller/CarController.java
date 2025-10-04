package fr.elecmove.api.controller;


import fr.elecmove.api.business.CarBusiness;
import fr.elecmove.api.controller.dto.car.CarCreationDTO;
import fr.elecmove.api.controller.dto.car.CarDTO;
import fr.elecmove.api.controller.dto.mapper.CarMapper;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.Car;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {

    CarBusiness carBusiness;
    CarMapper carMapper;

    public CarController(CarBusiness carBusiness, CarMapper carMapper) {
        this.carBusiness = carBusiness;
        this.carMapper = carMapper;
    }

    @GetMapping("/{id}")
    public CarDTO getCar(@PathVariable String id) {
        return carMapper.toDto(carBusiness.getCar(id));
    }


    @GetMapping
    public List<CarDTO> getAllCares(@AuthenticationPrincipal UserDetails userDetails) {
        List<CarDTO> carDTOS = new ArrayList<>();
        for(Car car : carBusiness.getAllCarByEmail(userDetails.getUsername())){
            carDTOS.add(carMapper.toDto(car));
        }
        return carDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CarDTO createCar(@RequestBody @Valid CarCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return carMapper.toDto(
                carBusiness.createCar(carMapper.toEntity(dto), user)
        );
    }


    @PutMapping("/{id}")
    public CarDTO updateCar(@PathVariable String id, @RequestBody CarCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return carMapper.toDto(
                carBusiness.updateCar(id, carMapper.toEntity(dto), user)
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCar(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        carBusiness.deleteCar(id, user);
    }

}
