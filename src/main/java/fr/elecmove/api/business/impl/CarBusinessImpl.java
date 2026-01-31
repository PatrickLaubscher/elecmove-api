package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.CarBusiness;
import fr.elecmove.api.business.mapper.CarEntityMapper;
import fr.elecmove.api.model.User;
import fr.elecmove.api.model.Car;
import fr.elecmove.api.repository.CarRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class CarBusinessImpl implements CarBusiness {

    private final CarRepository carRepository;
    private final CarEntityMapper carEntityMapper;


    public CarBusinessImpl(CarRepository carRepository, CarEntityMapper carEntityMapper) {
        this.carRepository = carRepository;
        this.carEntityMapper = carEntityMapper;
    }

    @Override
    public Car createCar(Car car, User user) {
        car.setUser(user);
        return carRepository.save(car);
    }


    @Override
    public Car getCar(String id) {
        return carRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The car does not exist")
        );
    }


    @Override
    public List<Car> getAllCarByEmail(String email) {
        return carRepository.findCarByUserEmail(email);
    }


    @Override
    public Car updateCar(String id, Car car, User user) {

        Car existingCar = carRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The car does not exist")
        );
        if(!existingCar.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't modify this car.");
        }

        carEntityMapper.merge(existingCar, car);

        return carRepository.save(existingCar);
    }


    @Override
    public void deleteCar(String id, User user) {

        Car existingCar = getCar(id);

        if(!existingCar.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this car.");
        }

        carRepository.delete(existingCar);
    }


}
