package fr.elecmove.api.controller;

import fr.elecmove.api.business.PictureBusiness;
import fr.elecmove.api.controller.dto.mapper.PictureMapper;
import fr.elecmove.api.controller.dto.picture.PictureDTO;
import fr.elecmove.api.model.Picture;
import fr.elecmove.api.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/stations/{stationId}/pictures")
public class PictureController {

    private final PictureBusiness pictureBusiness;
    private final PictureMapper pictureMapper;

    public PictureController(PictureBusiness pictureBusiness, PictureMapper pictureMapper) {
        this.pictureBusiness = pictureBusiness;
        this.pictureMapper = pictureMapper;
    }

    /**
     * Upload une ou plusieurs images pour une station
     * POST /api/stations/{stationId}/pictures
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public List<PictureDTO> uploadPictures(
            @PathVariable String stationId,
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal User user) {

        List<Picture> pictures = pictureBusiness.uploadPictures(stationId, files, user);
        return pictureMapper.toDtoList(pictures);
    }

    /**
     * Récupère toutes les images d'une station
     * GET /api/stations/{stationId}/pictures
     */
    @GetMapping
    public List<PictureDTO> getPictures(@PathVariable String stationId) {
        List<Picture> pictures = pictureBusiness.getPicturesByStation(stationId);
        return pictureMapper.toDtoList(pictures);
    }

    /**
     * Récupère une image spécifique
     * GET /api/stations/{stationId}/pictures/{pictureId}
     */
    @GetMapping("/{pictureId}")
    public PictureDTO getPicture(@PathVariable String stationId, @PathVariable String pictureId) {
        Picture picture = pictureBusiness.getPicture(pictureId);
        return pictureMapper.toDto(picture);
    }

    /**
     * Supprime une image
     * DELETE /api/stations/{stationId}/pictures/{pictureId}
     */
    @DeleteMapping("/{pictureId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePicture(
            @PathVariable String stationId,
            @PathVariable String pictureId,
            @AuthenticationPrincipal User user) {

        pictureBusiness.deletePicture(pictureId, user);
    }

    /**
     * Définit une image comme image principale
     * PATCH /api/stations/{stationId}/pictures/{pictureId}/main
     */
    @PatchMapping("/{pictureId}/main")
    public PictureDTO setMainPicture(
            @PathVariable String stationId,
            @PathVariable String pictureId,
            @AuthenticationPrincipal User user) {

        Picture picture = pictureBusiness.setMainPicture(pictureId, user);
        return pictureMapper.toDto(picture);
    }
}
