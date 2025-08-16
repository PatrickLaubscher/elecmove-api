package fr.elecmove.api.business;

import fr.elecmove.api.model.Booking;
import fr.elecmove.api.model.Car;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;

import java.util.List;

public interface BookingBusiness {


    /**
     *
     * @param booking
     * @param station
     * @param user
     * @param car
     * @return
     */
    Booking createBooking(Booking booking, Station station, User user, Car car);


    /**
     *
     * @param id
     * @return
     */
    Booking getBooking(String id);


    /**
     *
     * @param email
     * @return
     */
    List<Booking> getAllBookingByEmail(String email);


    /**
     *
     * @param id
     * @param booking
     * @param user
     * @return
     */
    Booking updateBooking(String id, Booking booking, User user);



    /**
     *
     * @param id
     * @param user
     * @return
     */
    void deleteBooking(String id, User user);

}
