package fr.elecmove.api.controller.dto.payment;

import fr.elecmove.api.controller.dto.booking.BookingDTO;

import java.time.LocalDateTime;

public class PaymentDTO {

    private String id;
    private Double amount;
    private LocalDateTime createdAt;
    private BookingDTO booking;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BookingDTO getBooking() {
        return booking;
    }

    public void setBooking(BookingDTO booking) {
        this.booking = booking;
    }
}
