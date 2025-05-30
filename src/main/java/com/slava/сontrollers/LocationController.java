package com.slava.сontrollers;

import com.slava.entities.User;
import com.slava.model.open_weather.Coordinates;
import com.slava.model.imodel.IWeather;
import com.slava.services.LocationService;
import com.slava.services.SessionService;
import com.slava.services.UserService;
import com.slava.services.WeatherService;
import com.slava.validators.InvalidSessionException;
import com.slava.validators.LocationAlreadyExistsException;
import com.slava.validators.LocationDeletionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class LocationController {
    private final LocationService locationService;
    private final SessionService sessionService;
    private final WeatherService weatherService;

    @Autowired
    public LocationController(LocationService locationService, SessionService sessionService, WeatherService weatherService, UserService userService) {
        this.locationService = locationService;
        this.sessionService = sessionService;
        this.weatherService = weatherService;
    }

    @PostMapping("/search_location")
    public String searchLocation(
            @CookieValue(value="session_id", defaultValue="") String sessionId,
            @RequestParam("cityName") String cityName,
            @RequestParam("sources") List<String> sources,
            Model model) {

        if (sessionId.isEmpty()) {
            return "redirect:/login";
        }

        if (sources == null || sources.isEmpty()) {
            model.addAttribute("error", "Выберите хотя бы один источник данных!");
            model.addAttribute("cityName", cityName);
            return "search";
        }

        Map<String, List<? extends IWeather>> resultsByApi = new HashMap<>();
        Map<String, String> errorsByApi = new HashMap<>();

        for (String source : sources) {
            try {
                List<? extends IWeather> weathers = weatherService.searchWeatherBySource(cityName, source);
                resultsByApi.put(source, weathers);
            } catch (Exception e) {
                errorsByApi.put(source, "Ошибка при получении данных: " + e.getMessage());
            }
        }

        model.addAttribute("resultsByApi", resultsByApi);
        model.addAttribute("errorsByApi", errorsByApi);
        model.addAttribute("cityName", cityName);
        model.addAttribute("selectedSources", sources);
        return "search";
    }


    @PostMapping("/save_location")
    public String saveLocation(@CookieValue(value = "session_id", defaultValue = "") String sessionId, @ModelAttribute("coordinates") Coordinates coordinates) {
        if (sessionId.isEmpty()){
            return "/login";
        }
        Optional<User> userBySession = sessionService.getUserBySessionId(sessionId);
        if (userBySession.isEmpty()) {
            throw new InvalidSessionException("Error saving location");
        }

        if (locationService.isLocationAlreadySavedByUser(coordinates, userBySession.get().getId())) {
            throw new LocationAlreadyExistsException("Location already saved");
        }
        locationService.saveLocation(coordinates, userBySession.get());
        return "redirect:/home";
    }

    @PostMapping("/delete_location")
    public String deleteLocation(@CookieValue(value = "session_id", defaultValue = "") String sessionId,
                                 @ModelAttribute("coordinates") Coordinates coordinates) {
        if (sessionId.isEmpty()){
            return "/login";
        }

        try {
            locationService.deleteLocation(coordinates);
            return "redirect:/home";
        } catch (Exception e) {
            throw new LocationDeletionException("Error saving location");
        }
    }

    @PostMapping("/change_api")
    public String changeApi(@CookieValue(value = "session_id", defaultValue = "") String sessionId,
                            @RequestParam(name="sources", defaultValue="WeatherRepository") String source,
                            Model model) {
        if (sessionId.isEmpty()){
            return "/login";
        }

        try {
            weatherService.switchRepository(source);
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", "Invalid weather source: " + source);
            return "redirect:/home";
        }

        return "redirect:/home";
    }
}
