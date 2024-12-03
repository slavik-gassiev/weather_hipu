package com.slava.services;

import com.slava.entities.Location;
import com.slava.entities.User;
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
    public void save(Location location) {
        locationRepository.save(location);
    }

    public List<Location> getUserLocations(Long userId) {
        return locationRepository.getLocationsByUser(userId);
    }
}
