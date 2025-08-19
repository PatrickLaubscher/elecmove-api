package fr.elecmove.api.controller;

import fr.elecmove.api.business.BookingBusiness;
import fr.elecmove.api.business.CarBusiness;
import fr.elecmove.api.business.StationBusiness;
import fr.elecmove.api.controller.dto.booking.BookingDTO;
import fr.elecmove.api.controller.dto.mapper.BookingMapper;
import fr.elecmove.api.controller.dto.booking.BookingCreationDTO;
import fr.elecmove.api.model.Booking;
import fr.elecmove.api.model.Car;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    BookingBusiness bookingBusiness;
    CarBusiness carBusiness;
    StationBusiness stationBusiness;
    BookingMapper bookingMapper;

    public BookingController(BookingBusiness bookingBusiness, CarBusiness carBusiness, StationBusiness stationBusiness, BookingMapper bookingMapper) {
        this.bookingBusiness = bookingBusiness;
        this.carBusiness = carBusiness;
        this.stationBusiness = stationBusiness;
        this.bookingMapper = bookingMapper;
    }

    @GetMapping("/{id}")
    public BookingDTO getBooking(@PathVariable String id) {
        return bookingMapper.toDto(bookingBusiness.getBooking(id));
    }


    @GetMapping
    public List<BookingDTO> getAllBookings(@AuthenticationPrincipal UserDetails user) {
        List<BookingDTO> stationDTOS = new ArrayList<>();
        for(Booking station : bookingBusiness.getAllBookingByEmail(user.getUsername())){
            stationDTOS.add(bookingMapper.toDto(station));
        }
        return stationDTOS;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDTO createBooking(@RequestBody @Valid BookingCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        Car car = carBusiness.getCar(dto.getCarId());
        Station station = stationBusiness.getStation(dto.getStationId());
        return bookingMapper.toDto(
                bookingBusiness.createBooking(bookingMapper.toEntity(dto), station, user, car)
        );
    }


    @PatchMapping("/{id}")
    public BookingDTO updateBooking(@PathVariable String id, @RequestBody BookingCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return bookingMapper.toDto(
                bookingBusiness.updateBooking(id, bookingMapper.toEntity(dto), user)
        );
    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBooking(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        bookingBusiness.deleteBooking(id, user);
    }

}
