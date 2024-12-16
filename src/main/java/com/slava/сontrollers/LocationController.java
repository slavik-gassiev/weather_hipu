package com.slava.сontrollers;

import com.slava.entities.User;
import com.slava.model.Coordinates;
import com.slava.model.Weather;
import com.slava.services.LocationService;
import com.slava.services.SessionService;
import com.slava.services.WeatherService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class LocationController {
    private LocationService locationService;
    private SessionService sessionService;
    private WeatherService weatherService;

    @Autowired
    public LocationController(LocationService locationService, SessionService sessionService, WeatherService weatherService) {
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.weatherService = weatherService;
    }

    @PostMapping("/search_location")
    public String searchLocation(@CookieValue(value = "session_id", defaultValue = "") String sessionId, @RequestParam(name = "cityName") String cityName,
                                 HttpServletResponse response,
                                 Model model) {
        if (sessionId.isEmpty()){
            return "redirect:/login";
        }

        List<Weather> weathers = weatherService.searchWeather(cityName);
        model.addAttribute("weathers", weathers);
        return "search";
    }

    @PostMapping("/save_location")
    public String saveLocation(@CookieValue(value = "session_id", defaultValue = "") String sessionId, @ModelAttribute("coordinates") Coordinates coordinates) {
        if (sessionId.isEmpty()){
            return "/login";
        }

        try {
            User userBySession = sessionService.getUserBySessionId(sessionId);
            locationService.saveLocation(coordinates, userBySession);
            return "redirect:/home";
        } catch (Exception e) {
            throw new RuntimeException("Error saving location");
        }
    }

    @PostMapping("/delete_location")
    public String deleteLocation(@CookieValue(value = "session_id", defaultValue = "") String sessionId, @RequestParam("id") Long id) {
        if (sessionId.isEmpty()){
            return "/login";
        }

        try {
            locationService.deleteLocation(id);
            return "redirect:/home";
        } catch (Exception e) {
            throw new RuntimeException("Error saving location");
        }
    }
}
