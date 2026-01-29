package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.BookingBusiness;
import fr.elecmove.api.business.mapper.BookingEntityMapper;
import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.BookingRepository;
import fr.elecmove.api.repository.BookingStatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;

@Service
@Transactional
public class BookingBusinessImpl implements BookingBusiness {

    BookingRepository bookingRepository;
    BookingEntityMapper bookingEntityMapper;
    BookingStatusRepository bookingStatusRepository;

    public BookingBusinessImpl(BookingRepository bookingRepository, BookingEntityMapper bookingEntityMapper,
                               BookingStatusRepository bookingStatusRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingEntityMapper = bookingEntityMapper;
        this.bookingStatusRepository = bookingStatusRepository;
    }

    @Override
    public Booking createBooking(Booking booking, Station station, User user, Car car) {
        booking.setStation(station);
        booking.setUser(user);
        booking.setCar(car);

        // Add awaiting status to new booking
        BookingStatus awaitingConfirmationStatus = bookingStatusRepository.findByName("En attente").get();
        booking.setStatus(awaitingConfirmationStatus);

        // Calculate total brut price
        Duration duration = Duration.between(booking.getStartTime(), booking.getEndTime());
        booking.setTotalPrice(duration.toHours() * station.getTarification());

        return bookingRepository.save(booking);
    }

    @Override
    public Booking getBooking(String id) {
        return bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The booking does not exist")
        );
    }

    @Override
    public List<Booking> getAllBookingByEmail(String email) {
        return bookingRepository.findByUserEmail(email);
    }

    @Override
    public List<Booking> getAllUpComingBookingByEmail(String email) {
        return bookingRepository.findUpcomingBookingsByUserEmail(email);
    }

    @Override
    public List<Booking> getAllPastBookingByEmail(String email) {
        return bookingRepository.findPastBookingsByUserEmail(email);
    }

    @Override
    public List<Booking> getAllBookingByStationId(String id) {
        return bookingRepository.findByStationId(id);
    }

    @Override
    public List<Booking> getAllBookingByEmailAndStatusId(String email, int statusId) {
        return bookingRepository.findByUserEmailAndStatusId(email, statusId);
    }

    @Override
    public List<Booking> getAllBookingByStationOwnerAndStatusId(String email, int statusId) {
        return bookingRepository.findByStationOwnerEmailAndStatusId(email, statusId);
    }

    @Override
    public Booking updateBooking(String id, Booking booking, User user) {

        Booking existingBooking = bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The booking does not exist")
        );
        if(!existingBooking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't modify this booking.");
        }

        bookingEntityMapper.merge(existingBooking, booking);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking updateBookingStatus(String id, int statusId) {

        Booking existingBooking = bookingRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The booking does not exist")
        );
        existingBooking.setStatus(
                bookingStatusRepository.findById(statusId).orElseThrow()
        );

        return bookingRepository.save(existingBooking);
    }

    @Override
    public void deleteBooking(String id, User user) {

        Booking existingBooking = getBooking(id);

        if(!existingBooking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this booking.");
        }

        bookingRepository.delete(existingBooking);

    }
}
