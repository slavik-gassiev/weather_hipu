package com.slava.сontrollers;

import com.slava.entities.Location;
import com.slava.model.Coordinates;
import com.slava.model.Weather;
import com.slava.services.LocationService;
import com.slava.services.WeatherService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LocationController {
    private LocationService locationService;
    private WeatherService weatherService;

    @Autowired
    public LocationController(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @PostMapping("/search_location")
    public String searchLocation(@RequestParam(name = "cityName") String cityName,
                                 HttpServletResponse response,
                                 Model model) {

        List<Weather> weathers = weatherService.searchWeather(cityName);
        model.addAttribute("weathers", weathers);
        return "search";
    }

    @PutMapping("/save_location")
    @ResponseBody
    public ResponseEntity<String> saveLocation(@RequestBody Coordinates coordinates) {
        try {
            locationService.saveLocation(coordinates.getName(), coordinates.getLat(), coordinates.getLon());
            return ResponseEntity.ok("Location saved successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error saving location");
        }
    }

    @DeleteMapping("/delete_location")
    @ResponseBody
    public ResponseEntity<String> deleteLocation(@RequestParam("id") Long id) {
        try {
            locationService.deleteLocation(id);
            return ResponseEntity.ok("Location deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting location");
        }
    }
}
