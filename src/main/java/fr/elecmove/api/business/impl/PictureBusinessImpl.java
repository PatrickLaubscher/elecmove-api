package fr.elecmove.api.business.impl;

import fr.elecmove.api.business.PictureBusiness;
import fr.elecmove.api.model.Picture;
import fr.elecmove.api.model.Station;
import fr.elecmove.api.model.User;
import fr.elecmove.api.repository.PictureRepository;
import fr.elecmove.api.repository.StationRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class PictureBusinessImpl implements PictureBusiness {

    private final PictureRepository pictureRepository;
    private final StationRepository stationRepository;

    @Value("${app.upload.dir:uploads}")
    private String uploadDir;

    @Value("${app.upload.base-url:/uploads}")
    private String baseUrl;

    private static final int MAX_WIDTH = 1200;
    private static final int THUMBNAIL_SIZE = 300;
    private static final List<String> ALLOWED_TYPES = List.of("image/jpeg", "image/png", "image/webp");

    public PictureBusinessImpl(PictureRepository pictureRepository, StationRepository stationRepository) {
        this.pictureRepository = pictureRepository;
        this.stationRepository = stationRepository;
    }

    @Override
    public List<Picture> uploadPictures(String stationId, List<MultipartFile> files, User user) {
        Station station = getStationAndCheckOwnership(stationId, user);

        List<Picture> pictures = new ArrayList<>();
        boolean isFirstPicture = pictureRepository.findByStationId(stationId).isEmpty();

        for (MultipartFile file : files) {
            validateFile(file);
            Picture picture = saveFile(file, station, isFirstPicture && pictures.isEmpty());
            pictures.add(picture);
        }

        return pictures;
    }

    @Override
    public List<Picture> getPicturesByStation(String stationId) {
        if (!stationRepository.existsById(stationId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Station not found");
        }
        return pictureRepository.findByStationId(stationId);
    }

    @Override
    public Picture getPicture(String pictureId) {
        return pictureRepository.findById(pictureId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Picture not found"));
    }

    @Override
    public void deletePicture(String pictureId, User user) {
        Picture picture = getPicture(pictureId);
        checkOwnership(picture.getStation(), user);

        // Supprimer les fichiers physiques
        deleteFiles(picture);

        pictureRepository.delete(picture);
    }

    @Override
    public Picture setMainPicture(String pictureId, User user) {
        Picture picture = getPicture(pictureId);
        Station station = picture.getStation();
        checkOwnership(station, user);

        // Retirer le flag main de toutes les autres images
        List<Picture> stationPictures = pictureRepository.findByStationId(station.getId());
        for (Picture p : stationPictures) {
            if (p.isMain()) {
                p.setMain(false);
                pictureRepository.save(p);
            }
        }

        // Définir cette image comme principale
        picture.setMain(true);
        return pictureRepository.save(picture);
    }

    private Station getStationAndCheckOwnership(String stationId, User user) {
        Station station = stationRepository.findById(stationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Station not found"));
        checkOwnership(station, user);
        return station;
    }

    private void checkOwnership(Station station, User user) {
        if (!station.getUser().getId().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You don't own this station");
        }
    }

    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File is empty");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Invalid file type. Allowed: " + String.join(", ", ALLOWED_TYPES));
        }

        // Limite de 10 MB
        if (file.getSize() > 10 * 1024 * 1024) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File too large. Max size: 10MB");
        }
    }

    private Picture saveFile(MultipartFile file, Station station, boolean isMain) {
        try {
            // Créer le dossier d'upload s'il n'existe pas
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Générer un nom unique
            String filename = UUID.randomUUID() + ".jpg";
            String thumbnailFilename = "thumb-" + filename;

            // Lire l'image
            BufferedImage originalImage = ImageIO.read(file.getInputStream());
            if (originalImage == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot read image file");
            }

            // Redimensionner et sauvegarder l'image principale
            BufferedImage resizedImage = resizeImage(originalImage, MAX_WIDTH);
            File outputFile = new File(uploadPath.toFile(), filename);
            ImageIO.write(resizedImage, "jpg", outputFile);

            // Créer et sauvegarder la miniature
            BufferedImage thumbnail = createThumbnail(originalImage, THUMBNAIL_SIZE);
            File thumbFile = new File(uploadPath.toFile(), thumbnailFilename);
            ImageIO.write(thumbnail, "jpg", thumbFile);

            // Créer l'entité Picture
            Picture picture = new Picture();
            picture.setSrc(baseUrl + "/" + filename);
            picture.setThumbnail(baseUrl + "/" + thumbnailFilename);
            picture.setAlt(station.getName());
            picture.setMain(isMain);
            picture.setStation(station);

            return pictureRepository.save(picture);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving file", e);
        }
    }

    private BufferedImage resizeImage(BufferedImage original, int maxWidth) {
        int width = original.getWidth();
        int height = original.getHeight();

        if (width <= maxWidth) {
            return original;
        }

        double ratio = (double) maxWidth / width;
        int newHeight = (int) (height * ratio);

        BufferedImage resized = new BufferedImage(maxWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resized.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, maxWidth, newHeight, null);
        g.dispose();

        return resized;
    }

    private BufferedImage createThumbnail(BufferedImage original, int size) {
        int width = original.getWidth();
        int height = original.getHeight();

        // Calculer les dimensions pour un crop carré centré
        int cropSize = Math.min(width, height);
        int x = (width - cropSize) / 2;
        int y = (height - cropSize) / 2;

        // Cropper au centre
        BufferedImage cropped = original.getSubimage(x, y, cropSize, cropSize);

        // Redimensionner
        BufferedImage thumbnail = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = thumbnail.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(cropped, 0, 0, size, size, null);
        g.dispose();

        return thumbnail;
    }

    private void deleteFiles(Picture picture) {
        try {
            // Extraire le nom du fichier depuis l'URL
            String src = picture.getSrc();
            String thumbnail = picture.getThumbnail();

            if (src != null) {
                String filename = src.substring(src.lastIndexOf('/') + 1);
                Path filePath = Paths.get(uploadDir, filename);
                Files.deleteIfExists(filePath);
            }

            if (thumbnail != null) {
                String thumbFilename = thumbnail.substring(thumbnail.lastIndexOf('/') + 1);
                Path thumbPath = Paths.get(uploadDir, thumbFilename);
                Files.deleteIfExists(thumbPath);
            }
        } catch (IOException e) {
            // Log l'erreur mais ne pas faire échouer la suppression
        }
    }
}
