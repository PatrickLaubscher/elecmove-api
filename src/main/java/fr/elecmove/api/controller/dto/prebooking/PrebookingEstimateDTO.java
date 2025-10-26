package fr.elecmove.api.controller.dto.prebooking;

import java.time.Duration;

public class PrebookingEstimateDTO {

    private Double bookingDuration;
    private Double bookingEstimatePrice;

    public PrebookingEstimateDTO(Duration bookingDuration, Double bookingEstimatePrice) {
        this.bookingDuration = bookingDuration.toMinutes() / 60.0;
        this.bookingEstimatePrice = bookingEstimatePrice;
    }

    public Double getBookingDuration() {
        return bookingDuration;
    }

    public void setBookingDuration(Double bookingDuration) {
        this.bookingDuration = bookingDuration;
    }

    public Double getBookingEstimatePrice() {
        return bookingEstimatePrice;
    }

    public void setBookingEstimatePrice(Double bookingEstimatePrice) {
        this.bookingEstimatePrice = bookingEstimatePrice;
    }
}
