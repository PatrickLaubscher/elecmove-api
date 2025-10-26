package fr.elecmove.api.controller.dto.prebooking;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public class PrebookingEstimateRequestDTO {

    @NotNull
    private LocalTime bookingStartTime;
    @NotNull
    private LocalTime bookingEndTime;

    public LocalTime getBookingStartTime() {
        return bookingStartTime;
    }

    public void setBookingStartTime(LocalTime bookingStartTime) {
        this.bookingStartTime = bookingStartTime;
    }

    public LocalTime getBookingEndTime() {
        return bookingEndTime;
    }

    public void setBookingEndTime(LocalTime bookingEndTime) {
        this.bookingEndTime = bookingEndTime;
    }
}
