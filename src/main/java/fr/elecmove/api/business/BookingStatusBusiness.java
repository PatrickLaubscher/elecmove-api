package fr.elecmove.api.business;

import fr.elecmove.api.model.BookingStatus;

import java.util.List;

public interface BookingStatusBusiness {

    /**
     *
     * @param id
     * @return
     */
    BookingStatus getBookingStatus(String id);


    /**
     *
     * @return
     */
    List<BookingStatus> getAllBookingStatus();
}
