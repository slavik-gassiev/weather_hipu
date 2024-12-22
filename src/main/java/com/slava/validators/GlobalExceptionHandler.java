package com.slava.validators;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidSessionException.class)
    public String handleInvalidSession(InvalidSessionException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(LocationAlreadyExistsException.class)
    public String handleLocationAlreadyExists(LocationAlreadyExistsException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(Exception.class)
    public String handleGeneralException(Exception ex, Model model) {
        model.addAttribute("errorMessage", "Произошла ошибка. Попробуйте снова.");
        return "error";
    }

    @ExceptionHandler(LocationDeletionException.class)
    public String handleLocationDeletionException(LocationDeletionException ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}

