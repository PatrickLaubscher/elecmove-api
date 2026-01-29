package fr.elecmove.api.controller;


import fr.elecmove.api.business.BookingStatusBusiness;
import fr.elecmove.api.controller.dto.booking_status.BookingStatusDTO;
import fr.elecmove.api.controller.dto.mapper.BookingStatusMapper;
import fr.elecmove.api.controller.dto.station.StationDTO;
import fr.elecmove.api.model.BookingStatus;
import fr.elecmove.api.model.Station;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/booking-status")
public class BookingStatusController {

    private final BookingStatusMapper bookingStatusMapper;
    BookingStatusBusiness bookingStatusBusiness;

    public BookingStatusController(BookingStatusBusiness bookingStatusBusiness, BookingStatusMapper bookingStatusMapper) {
        this.bookingStatusBusiness = bookingStatusBusiness;
        this.bookingStatusMapper = bookingStatusMapper;
    }

    @GetMapping("/{id}")
    public BookingStatusDTO getBookingStatus(@PathVariable int id) {
        return bookingStatusMapper.toDto(bookingStatusBusiness.getBookingStatus(id));
    }


    @GetMapping
    public List<BookingStatusDTO> getAllBookingStatus() {
        List<BookingStatusDTO> bookingStatusDTOS = new ArrayList<>();
        for(BookingStatus status: bookingStatusBusiness.getAllBookingStatus()){
            bookingStatusDTOS.add(bookingStatusMapper.toDto(status));
        }
        return bookingStatusDTOS;
    }
}
