package fr.elecmove.api.business;

import fr.elecmove.api.model.User;
import fr.elecmove.api.model.Car;

import java.util.List;

public interface CarBusiness {

    /**
     *
     * @param car
     * @param user
     * @return
     */
    Car createCar(Car car, User user);


    /**
     *
     * @param id
     * @return
     */
    Car getCar(String id);


    /**
     *
     * @param email
     * @return
     */
    List<Car> getAllCarByEmail(String email);


    /**
     *
     * @param id
     * @param car
     * @param user
     * @return
     */
    Car updateCar(String id, Car car, User user);


    /**
     *
     * @param id
     * @return
     */
    void deleteCar(String id, User user);

}
