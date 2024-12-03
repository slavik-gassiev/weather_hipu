package com.slava.сontrollers;

import com.slava.entities.Location;
import com.slava.entities.User;
import com.slava.model.Weather;
import com.slava.services.LocationService;
import com.slava.services.SessionService;
import com.slava.services.UserService;
import com.slava.services.WeatherService;
import jakarta.servlet.http.Cookie;
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

import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final SessionService sessionService;
    private final LocationService locationService;
    private final WeatherService weatherService;
    private final UserService userService;

    @Autowired
    public UserController(SessionService sessionService, LocationService locationService, WeatherService weatherService, UserService userService) {
        this.sessionService = sessionService;
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.userService = userService;
    }

    @GetMapping("/")
    public String mainHomePage(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (sessionId.isEmpty()){
            return "redirect:/login";
        }

        User user = sessionService.getUserBySession(sessionId);
        List<Location> locations = locationService.getUserLocations(user.getId());
        List<Weather>  weathers = weatherService.getWeathersByLocations(locations);
        String login = user.getLogin();
        model.addAttribute("weathers", weathers);
        model.addAttribute("login", login);
        return "home";
    }

    @GetMapping("/login")
    public String login(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (!sessionId.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "redirect:/login";
        }

        if (userService.findByLogin(user.getLogin()).isEmpty()) {
            return "redirect:/login";
        }

        Optional<User> foundUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (foundUser.isEmpty()) {
            return "redirect:/login";
        }
        String userUuid = sessionService.saveSession(foundUser.get()).toString();
        Cookie cookie = new Cookie("session_id", userUuid);
        response.addCookie(cookie);

        return "/";
    }

    @GetMapping("/authorization")
    public String authorization(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (!sessionId.isEmpty()) {
            return "redirect:/";
        }

        model.addAttribute("user", new User());
        return "/authorization";
    }

    @PostMapping("/authorization")
    public String authorization(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "redirect:/login";
        }

        if (userService.findByLogin(user.getLogin()).isPresent()) {
            return "redirect:/login";
        }

        User createdUser = userService.saveAndGetUser(user.getLogin(), user.getPassword());
        String userUuid = sessionService.saveSession(createdUser).toString();
        Cookie cookie = new Cookie("session_id", userUuid);
        response.addCookie(cookie);

        return "/";
    }

    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("session_id", "");
        response.addCookie(cookie);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "/logout";
    }
}
