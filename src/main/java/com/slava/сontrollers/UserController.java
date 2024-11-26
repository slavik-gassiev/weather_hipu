package com.slava.сontrollers;

import com.slava.entities.Location;
import com.slava.entities.User;
import com.slava.model.Weather;
import com.slava.services.LocationService;
import com.slava.services.SessionService;
import com.slava.services.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class UserController {

    private final SessionService sessionService;
    private final LocationService locationService;
    private final WeatherService weatherService;

    @Autowired
    public UserController(SessionService sessionService, LocationService locationService, WeatherService weatherService) {
        this.sessionService = sessionService;
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @GetMapping("/")
    public String mainHomePage(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (sessionId.isEmpty()){
            return "redirect:/login";
        }

        User user = sessionService.getUserBySession(sessionId);
        List<Location> locations = locationService.getUserLocations(user);
        List<Weather>  weathers = weatherService.getWeathersByLocations(locations);
        String login = user.getLogin();
        model.addAttribute("weathers", weathers);
        model.addAttribute("login", login);
        return "home";
    }

}
