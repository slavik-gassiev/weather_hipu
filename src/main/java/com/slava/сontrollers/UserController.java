package com.slava.сontrollers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/")
    public String mainHomePage(@CookieValue(value = "session_id", defaultValue = "") String sessionId, Model model) {
        return "home";
    }

}
