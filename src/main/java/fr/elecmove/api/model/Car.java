package fr.elecmove.api.model;


import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "car")
public class Car {

    @Id
    @UuidGenerator
    private String id;
    private String type;
    private String registration;
    private String brand;

    @ManyToOne
    private User user;

    public Car() {
    }

    public Car(String id, String type, String registration, String brand, User user) {
        this.id = id;
        this.type = type;
        this.registration = registration;
        this.brand = brand;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
