package fr.elecmove.api.model;


import jakarta.persistence.*;

@Entity
@Table(name = "booking_status")
public class BookingStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, updatable = false, unique = true)
    private String name;

    public BookingStatus() {
    }

    public BookingStatus(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
