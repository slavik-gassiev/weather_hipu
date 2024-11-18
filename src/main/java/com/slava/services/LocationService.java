package com.slava.services;

import com.slava.entities.Location;
import com.slava.entities.User;
import com.slava.repositories.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LocationService {
    LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Transactional
    public void save(Location location) {
        locationRepository.save(location);
    }

    public List<Location> getUserLocations(User user) {
        return locationRepository.getLocationByUser(user);
    }
}
