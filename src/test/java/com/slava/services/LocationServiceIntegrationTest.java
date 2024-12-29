
package com.slava.services;

import com.slava.config.TestConfig;
import com.slava.entities.Location;
import com.slava.entities.User;
import com.slava.model.Coordinates;
import com.slava.repositories.ILocationRepository;
import com.slava.repositories.IUserRepository;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringJUnitConfig(TestConfig.class)
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class LocationServiceIntegrationTest {

    @Autowired
    private Flyway flyway;

    @Autowired
    private LocationService locationService;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ILocationRepository locationRepository;

    @BeforeEach
    void setUp() {
        // Очистка базы данных перед каждым тестом
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void saveLocation_savesLocationForUser() {
        User user = new User();
        user.setLogin("locationUser");
        user.setPassword("password");
        userRepository.save(user);

        Location location = new Location("City A", 10.0, 20.0);
        location.setUser(user);

        locationService.saveLocation(location);

        List<Location> userLocations = locationRepository.getLocationsByUserId(user.getId());
        assertEquals(1, userLocations.size());
        assertEquals("City A", userLocations.get(0).getName());
    }

    @Test
    void deleteLocation_removesLocation() {
        User user = new User();
        user.setLogin("deleteUser");
        user.setPassword("password");
        userRepository.save(user);

        Location location = new Location("City B", 30.0, 40.0);
        location.setUser(user);
        locationRepository.save(location);

        assertEquals(1, locationRepository.getLocationsByUserId(user.getId()).size());
        locationService.deleteLocation(location.getId());
        assertEquals(0, locationRepository.getLocationsByUserId(user.getId()).size());
    }

    @Test
    void isLocationAlreadySavedByUser_returnsTrueIfExists() {
        User user = new User();
        user.setLogin("existsUser");
        user.setPassword("password");
        userRepository.save(user);

        Location location = new Location("City C", 50.0, 60.0);
        location.setUser(user);
        locationRepository.save(location);

        boolean isSaved = locationService.isLocationAlreadySavedByUser(
                new Coordinates("City C", 50.0, 60.0),
                user.getId()
        );
        assertTrue(isSaved);
    }

    @Test
    void isLocationAlreadySavedByUser_returnsFalseIfNotExists() {
        User user = new User();
        user.setLogin("notExistsUser");
        user.setPassword("password");
        userRepository.save(user);

        boolean isSaved = locationService.isLocationAlreadySavedByUser(
                new Coordinates("Non-existent City", 70.0, 80.0),
                user.getId()
        );
        assertFalse(isSaved);
    }

    @Test
    void getUserLocations_returnsLocationsForUser() {
        User user = new User();
        user.setLogin("locationsUser");
        user.setPassword("password");
        userRepository.save(user);

        Location location1 = new Location("City D", 90.0, 100.0);
        location1.setUser(user);
        locationRepository.save(location1);

        Location location2 = new Location("City E", 110.0, 120.0);
        location2.setUser(user);
        locationRepository.save(location2);

        List<Location> locations = locationService.getUserLocations(user.getId());
        assertEquals(2, locations.size());
        assertEquals("City D", locations.get(0).getName());
        assertEquals("City E", locations.get(1).getName());
    }
}

