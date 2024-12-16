package com.slava.services;

import com.slava.entities.Location;
import com.slava.entities.User;
import com.slava.model.Coordinates;
import com.slava.repositories.ILocationRepository;
import com.slava.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class LocationService {
    ILocationRepository locationRepository;
    IUserRepository userRepository;

    @Autowired
    public LocationService(ILocationRepository ILocationRepository, IUserRepository userRepository) {
        this.locationRepository = ILocationRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveLocation(Location location) {
        locationRepository.save(location);
    }

    @Transactional
    public void saveLocation(Coordinates coordinates, User user) {
        Location location = new Location(coordinates.getName(), coordinates.getLat(), coordinates.getLon());
        location.setUser(user);
        locationRepository.save(location);
    }

    @Transactional
    public void deleteLocation(Long locationId) {
        locationRepository.deleteById(locationId);
    }

    public List<Location> getUserLocations(Long userId) {
        return locationRepository.getLocationsByUserId(userId);
    }
}
