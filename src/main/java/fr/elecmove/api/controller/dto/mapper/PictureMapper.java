package fr.elecmove.api.controller.dto.mapper;

import fr.elecmove.api.controller.dto.picture.PictureDTO;
import fr.elecmove.api.model.Picture;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PictureMapper {

    PictureDTO toDto(Picture picture);

    List<PictureDTO> toDtoList(List<Picture> pictures);
}
