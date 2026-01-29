package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.BookingStatusBusiness;
import fr.elecmove.api.model.BookingStatus;
import fr.elecmove.api.repository.BookingStatusRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@Transactional
public class BookingStatusBusinessImpl implements BookingStatusBusiness {

    private final BookingStatusRepository bookingStatusRepository;

    public BookingStatusBusinessImpl(BookingStatusRepository bookingStatusRepository) {
        this.bookingStatusRepository = bookingStatusRepository;
    }

    @Override
    public BookingStatus getBookingStatus(int id) {
        return bookingStatusRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The car does not exist")
        );
    }

    @Override
    public List<BookingStatus> getAllBookingStatus() {
        return bookingStatusRepository.findAll();
    }
}
