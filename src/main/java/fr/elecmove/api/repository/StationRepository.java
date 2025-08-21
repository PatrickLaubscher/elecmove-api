package fr.elecmove.api.repository;

import fr.elecmove.api.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, String> {

    List<Station> findStationByUserEmail(String email);

    @Query(value = """
    SELECT s.*
    FROM station s
    JOIN location_station ls ON s.location_id = ls.id
    WHERE s.is_available = TRUE
      AND ST_Distance_Sphere(
            ST_SRID(POINT(ls.longitude, ls.latitude), 4326),
            ST_SRID(POINT(:longitude, :latitude), 4326)
          ) <= :rayon
    ORDER BY ST_Distance_Sphere(
            ST_SRID(POINT(ls.longitude, ls.latitude), 4326),
            ST_SRID(POINT(:longitude, :latitude), 4326)
          ) ASC
    """, nativeQuery = true)
    List<Station> findStationsNearby(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("rayon") double rayonMeters
    );



}
