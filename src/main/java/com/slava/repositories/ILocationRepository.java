package com.slava.repositories;

import com.slava.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ILocationRepository extends JpaRepository<Location, Long> {

    List<Location> getLocationsByUserId(Long userId);
}
