package com.slava.services;

import com.slava.entities.Location;
import com.slava.repositories.ILocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {
    ILocationRepository locationRepository;

    @Autowired
    public LocationService(ILocationRepository ILocationRepository) {
        this.locationRepository = ILocationRepository;
    }

    @Transactional
    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    @Transactional
    public void saveLocation(String name, Double lat, Double lon) {
        locationRepository.save(new Location(name, lat, lon));
    }

    @Transactional
    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public List<Location> getUserLocations(Long userId) {
        return locationRepository.getLocationsByUserId(userId);
    }
}
