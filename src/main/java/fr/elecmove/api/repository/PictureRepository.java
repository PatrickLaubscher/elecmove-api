package fr.elecmove.api.repository;

import fr.elecmove.api.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PictureRepository extends JpaRepository<Picture, String> {
    List<Picture> findByStationId(String stationId);
}
