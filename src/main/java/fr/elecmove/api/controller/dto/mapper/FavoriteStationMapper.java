package fr.elecmove.api.controller.dto.mapper;


import fr.elecmove.api.controller.dto.favorite_station.FavoriteStationDTO;
import fr.elecmove.api.model.FavoriteStation;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;


@Mapper(componentModel= MappingConstants.ComponentModel.SPRING)
public interface FavoriteStationMapper {

    FavoriteStationDTO toDto(FavoriteStation favoriteStation);

}
