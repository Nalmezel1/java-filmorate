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
public class UserController {
    private Map<Long, User> users = new HashMap<>();
    long currentMaxId = 0;

    @PostMapping(value = "/users")
    public User createUser(@Valid @RequestBody User user) {
        user = checkUserName(user);

        currentMaxId++;
        user.setId(currentMaxId);

        users.put(user.getId(), user);

        log.info(String.format("Пользователь c user_id=%d создан", user.getId()));
        log.debug(String.format("info пользователя = %s", user.toString()));
        return user;
    }

    @PutMapping(value = "/users")
    public User updateUser(@Valid @RequestBody User user) throws ValidationException {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException(String.format("Пользователь с id= %d не найден", user.getId()));
        }
        user = checkUserName(user);

        users.put(user.getId(), user);

        log.info(String.format("Пользователь c user_id=%d обновлен", user.getId()));
        log.debug(String.format("info пользователя = %s", user.toString()));
        return user;
    }

    @GetMapping("/users")
    public List<User> listAllUsers() {
        return List.copyOf(users.values());
    }

    private User checkUserName(User user) {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn(String.format("Name пустой, присваивается занчение login: %s", user.getLogin()));
            user.setName(user.getLogin());
        }
        return user;
    }
}
