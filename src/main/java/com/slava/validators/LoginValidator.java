package com.slava.validators;

import com.slava.entities.User;
import com.slava.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class LoginValidator implements Validator {
    private final UserService userService;

    @Autowired
    public LoginValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (userService.findByLogin(user.getLogin()).isEmpty()) {
            errors.rejectValue("login", "error.user", "Данный логин не существует. Выберите другой.");
        }

        if (userService.findByLoginAndPassword(user.getLogin(), user.getPassword()).isEmpty()) {
            errors.rejectValue("password", "error.user", "Вы ввели неверный логин или пароль.");
        }
    }
}
