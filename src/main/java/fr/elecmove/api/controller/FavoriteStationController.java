package fr.elecmove.api.controller;


import fr.elecmove.api.business.FavoriteStationBusiness;
import fr.elecmove.api.controller.dto.favorite_station.FavoriteStationCreationDTO;
import fr.elecmove.api.controller.dto.favorite_station.FavoriteStationDTO;
import fr.elecmove.api.controller.dto.mapper.FavoriteStationMapper;
import fr.elecmove.api.model.FavoriteStation;
import fr.elecmove.api.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/favorite-stations")
public class FavoriteStationController {

    FavoriteStationBusiness favoriteStationBusiness;
    FavoriteStationMapper favoriteStationMapper;

    public FavoriteStationController(FavoriteStationBusiness favoriteStationBusiness, FavoriteStationMapper favoriteStationMapper) {
        this.favoriteStationBusiness = favoriteStationBusiness;
        this.favoriteStationMapper = favoriteStationMapper;
    }

    @GetMapping("/{id}")
    public FavoriteStationDTO getFavoriteStation(@PathVariable String id) {
        return favoriteStationMapper.toDto(favoriteStationBusiness.getFavoriteStation(id));
    }

    @GetMapping
    public List<FavoriteStationDTO> getAllFavoriteStations(@AuthenticationPrincipal UserDetails userDetails) {
        List<FavoriteStationDTO> favoriteStationDTOS = new ArrayList<>();

        for(FavoriteStation favoriteStation : favoriteStationBusiness.getAllFavouriteStation(userDetails.getUsername())){
            favoriteStationDTOS.add(favoriteStationMapper.toDto(favoriteStation));
        }
        return favoriteStationDTOS;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public FavoriteStationDTO createFavoriteStation(@RequestBody FavoriteStationCreationDTO dto, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        return favoriteStationMapper.toDto(
                favoriteStationBusiness.createFavoriteStation(dto.getStationId(), user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteFavoriteStation(@PathVariable String id, @AuthenticationPrincipal UserDetails userDetails) {
        User user = (User) userDetails;
        favoriteStationBusiness.deleteFavoriteStation(id, user);
    }

}
