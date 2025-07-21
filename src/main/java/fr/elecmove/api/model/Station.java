package fr.elecmove.api.model;


import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "station", schema = "elecmove")
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Double tarification;
    private String power;
    private String instrucion;
    private String picture;
    private String video;

    @Column(name = "is_free_standing")
    private boolean isFreeStanding;

    @Column(name = "is_available")
    private boolean is_available;

    private String type;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
