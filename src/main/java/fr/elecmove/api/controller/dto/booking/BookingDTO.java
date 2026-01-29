package fr.elecmove.api.controller.dto.booking;

import com.fasterxml.jackson.annotation.JsonFormat;
import fr.elecmove.api.controller.dto.booking_status.BookingStatusDTO;
import fr.elecmove.api.controller.dto.car.CarDTO;
import fr.elecmove.api.controller.dto.payment.PaymentSingleDTO;
import fr.elecmove.api.controller.dto.station.StationDTO;
import fr.elecmove.api.controller.dto.station.StationSingleDTO;
import fr.elecmove.api.controller.dto.user.UserSingleDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class BookingDTO {

    private String id;
    private LocalDate date;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime endTime;
    private Double totalPrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserSingleDTO user;
    private CarDTO car;
    private StationSingleDTO station;
    private BookingStatusDTO status;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UserSingleDTO getUser() {
        return user;
    }

    public void setUser(UserSingleDTO user) {
        this.user = user;
    }

    public CarDTO getCar() {
        return car;
    }

    public void setCar(CarDTO car) {
        this.car = car;
    }

    public StationSingleDTO getStation() {
        return station;
    }

    public void setStation(StationSingleDTO station) {
        this.station = station;
    }

    public BookingStatusDTO getStatus() {
        return status;
    }

    public void setStatus(BookingStatusDTO status) {
        this.status = status;
    }

}
