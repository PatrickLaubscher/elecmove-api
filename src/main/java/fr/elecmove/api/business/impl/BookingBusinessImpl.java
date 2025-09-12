package fr.elecmove.api.business.impl;


import fr.elecmove.api.business.BookingBusiness;
import fr.elecmove.api.business.mapper.BookingEntityMapper;
import fr.elecmove.api.model.*;
import fr.elecmove.api.repository.BookingRepository;
import fr.elecmove.api.repository.BookingStatusRepository;
import jdk.jshell.Snippet;
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

    public BookingBusinessImpl(BookingRepository bookingRepository, BookingEntityMapper bookingEntityMapper, BookingStatusRepository bookingStatusRepository) {
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
    public void deleteBooking(String id, User user) {

        Booking existingBooking = getBooking(id);

        if(!existingBooking.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't delete this booking.");
        }

        bookingRepository.delete(existingBooking);

    }
}
