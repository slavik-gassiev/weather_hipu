package com.slava.сontrollers;

import com.slava.entities.Location;
import com.slava.entities.User;
import com.slava.model.Weather;
import com.slava.services.LocationService;
import com.slava.services.SessionService;
import com.slava.services.UserService;
import com.slava.services.WeatherService;
import com.slava.validators.LoginValidator;
import com.slava.validators.RegistrationValidator;
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
    private final LoginValidator loginValidator;
    private final RegistrationValidator registrationValidator;

    @Autowired
    public UserController(SessionService sessionService, LocationService locationService, WeatherService weatherService, UserService userService, LoginValidator LoginValidator, RegistrationValidator registrationValidator) {
        this.sessionService = sessionService;
        this.locationService = locationService;
        this.weatherService = weatherService;
        this.userService = userService;
        this.loginValidator = LoginValidator;
        this.registrationValidator = registrationValidator;
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
    public String login(@ModelAttribute("user") User user, BindingResult bindingResult, HttpServletResponse response) {
        loginValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/login";
        }

        Optional<User> foundUser = userService.findByLogin(user.getLogin());
        String sessionUuid = sessionService.getSessionUuid(foundUser.get())
                .orElseGet(() -> sessionService.saveSession(foundUser.get()))
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
    public String registration(@ModelAttribute("user") User user, BindingResult bindingResult, HttpServletResponse response) {
        registrationValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
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
