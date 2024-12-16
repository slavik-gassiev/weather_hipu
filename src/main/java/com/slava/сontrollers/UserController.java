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

    @GetMapping("/home")
    public String mainHomePage(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (sessionId.isEmpty()){
            return "redirect:/login";
        }

        User user = sessionService.getUserBySessionId(sessionId);
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
            return "/home";
        }

        model.addAttribute("user", new User());
        return "/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "/login";
        }

        Optional<User> foundUser = userService.findByLogin(user.getLogin());
        if (foundUser.isEmpty()) {
            bindingResult.rejectValue("login", "error.user", "Данный логин не существует. Выберите другой.");
            return "/login";
        }

        foundUser = userService.findByLoginAndPassword(user.getLogin(), user.getPassword());
        if (foundUser.isEmpty()) {
            bindingResult.rejectValue("login", "error.user", "Вы ввели неверный логин или пароль.");
            return "/login";
        }

        User userEntity = foundUser.get();
        String sessionUuid = sessionService.getSessionUuid(userEntity)
                .orElseGet(() -> sessionService.saveSession(userEntity))
                .toString();

        Cookie cookie = new Cookie("session_id", sessionUuid);
        response.addCookie(cookie);

        return "redirect:/home";
    }

    @GetMapping("/registration")
    public String registration(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        if (!sessionId.isEmpty()) {
            return "redirect:/home";
        }

        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("user") @Valid User user, BindingResult bindingResult, HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "/registration";
        }

        if (userService.findByLogin(user.getLogin()).isPresent()) {
            bindingResult.rejectValue("login", "error.user", "Данный логин уже занят. Выберите другой.");
            return "/registration";
        }

        User createdUser = userService.saveAndGetUser(user);
        String userUuid = sessionService.saveSession(createdUser).toString();
        Cookie cookie = new Cookie("session_id", userUuid);
        response.addCookie(cookie);

        return "/home";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("session_id", "");
        response.addCookie(cookie);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "/logout";
    }
}
