package fr.elecmove.api.repository;

import fr.elecmove.api.model.Picture;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PictureRepository extends JpaRepository<Picture, String> {
}
