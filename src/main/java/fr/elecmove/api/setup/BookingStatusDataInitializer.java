package fr.elecmove.api.setup;


import fr.elecmove.api.model.BookingStatus;
import fr.elecmove.api.repository.BookingStatusRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusDataInitializer implements CommandLineRunner {

    private final BookingStatusRepository statusRepository;

    public BookingStatusDataInitializer(BookingStatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if  (statusRepository.count() == 0) {
            BookingStatus status1 = new BookingStatus();
            status1.setName("En attente");

            BookingStatus status2 = new BookingStatus();
            status2.setName("Confirmé");

            BookingStatus status3 = new BookingStatus();
            status3.setName("Payé");

            statusRepository.save(status1);
            statusRepository.save(status2);
            statusRepository.save(status3);

        }
    }
}