package fr.elecmove.api.business;

import fr.elecmove.api.model.Picture;
import fr.elecmove.api.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PictureBusiness {

    /**
     * Upload une ou plusieurs images pour une station
     * @param stationId ID de la station
     * @param files fichiers images
     * @param user utilisateur authentifié
     * @return liste des pictures créées
     */
    List<Picture> uploadPictures(String stationId, List<MultipartFile> files, User user);

    /**
     * Récupère toutes les images d'une station
     * @param stationId ID de la station
     * @return liste des pictures
     */
    List<Picture> getPicturesByStation(String stationId);

    /**
     * Récupère une image par son ID
     * @param pictureId ID de l'image
     * @return picture
     */
    Picture getPicture(String pictureId);

    /**
     * Supprime une image
     * @param pictureId ID de l'image
     * @param user utilisateur authentifié
     */
    void deletePicture(String pictureId, User user);

    /**
     * Définit une image comme image principale de la station
     * @param pictureId ID de l'image
     * @param user utilisateur authentifié
     * @return picture mise à jour
     */
    Picture setMainPicture(String pictureId, User user);
}
