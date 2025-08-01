package fr.elecmove.api.model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.hibernate.annotations.UuidGenerator;

@Entity
@Table(name = "role", schema = "elecmove")
public class Role {

    @Id
    @UuidGenerator
    private String id;

    @Column(nullable = false, unique = true)
    private String name; // Exemple : "ADMIN", "CUSTOMER"


    public Role(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Role() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }


}
