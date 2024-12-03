package com.slava.сontrollers;

import com.slava.entities.Location;
import com.slava.services.LocationService;
import com.slava.services.WeatherService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
    public String searchLocation(@ModelAttribute("location") @Valid Location location, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "redirect:/search_location";
        }
        return "";
    }
}
