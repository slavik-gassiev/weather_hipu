package com.slava.сontrollers;

import com.slava.entities.Location;
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

@Controller
public class LocationController {
    private LocationService locationService;
    private WeatherService weatherService;

    @Autowired
    public LocationController(LocationService locationService, WeatherService weatherService) {
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @GetMapping("/search_location")
    public String searchLocation(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (sessionId.isEmpty()) {
            return "/login";
        }

        model.addAttribute("toSearchLocation", new Location());
        return "";
    }

    @PostMapping("/search_location")
    public String searchLocation(@ModelAttribute("location") @Valid Location location, BindingResult bindingResult,
                                 HttpServletResponse response, Model model) {
        if (bindingResult.hasErrors()) {
            return "redirect:/search_location";
        }
        if (location.getName().isBlank() || location.getName().isEmpty()) {
            throw new RuntimeException("You wrote empty location");
        }
        Weather weather = weatherService.searchWeather(location.getName());
        model.addAttribute("weather", weather);
        return "seatch";
    }

    @PutMapping("/save_location")
    @ResponseBody
    public ResponseEntity<String> saveLocation(@RequestBody Weather weather) {
        try {
            locationService.saveLocation(weather.getName(), weather.getLat(), weather.getLon());
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
