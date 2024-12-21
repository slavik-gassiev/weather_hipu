package com.slava.validators;

import com.slava.entities.User;
import com.slava.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegistrationValidator implements Validator {
    private final UserService userService;

    @Autowired
    public RegistrationValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz); // Проверяет, что класс, который нужно валидировать, — это User
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.findByLogin(user.getLogin()).isPresent()) {
            errors.rejectValue("login", "error.user", "Логин уже занят. Выберите другой.");
        }

        if (user.getPassword() != null && user.getPassword().length() < 6) {
            errors.rejectValue("password", "error.user", "Пароль должен содержать не менее 6 символов.");
        }

        if (user.getPassword() != null && !user.getPassword().matches(".*[@#$%^&+=!].*")) {
            errors.rejectValue("password", "error.user", "Пароль должен содержать хотя бы один специальный символ.");
        }
    }
}

