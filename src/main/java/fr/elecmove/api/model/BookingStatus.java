package fr.elecmove.api.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "booking_status")
public class BookingStatus {

    @Id
    @UuidGenerator
    private String id;
    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    public BookingStatus() {
    }

    public BookingStatus(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
