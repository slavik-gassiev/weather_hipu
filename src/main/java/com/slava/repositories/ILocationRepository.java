package com.slava.repositories;

import com.slava.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long> {

    List<Location> getLocationsByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM Location l WHERE l.latitude = :latitude AND l.longitude = :longitude")
    void deleteByLatitudeAndLongitude(@Param("latitude") Double latitude, @Param("longitude") Double longitude);

    Optional<Location> findByLatitudeAndLongitudeAndUser_Id(Double latitude, Double longitude, Long userId);
}
