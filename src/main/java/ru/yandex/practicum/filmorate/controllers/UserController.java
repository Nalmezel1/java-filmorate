package ru.yandex.practicum.filmorate.controllers;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends AbstractController<User> {

    @Override
    public void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn(String.format("Name пустой, присваивается занчение login: %s", user.getLogin()));
            user.setName(user.getLogin());
        }
        if (!user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            throw new ValidationException("Не корректный email");
        }

    }
}
