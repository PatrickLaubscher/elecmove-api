package fr.elecmove.api.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "picture")
public class Picture {

    @Id
    @UuidGenerator
    private String id;
    private String alt;
    private String src;
    private boolean main;

    @ManyToOne
    private Station station;



    public Picture() {
    }
    public Picture(String id, String alt, String src, boolean main, Station station) {
        this.id = id;
        this.alt = alt;
        this.src = src;
        this.main = main;
        this.station = station;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
