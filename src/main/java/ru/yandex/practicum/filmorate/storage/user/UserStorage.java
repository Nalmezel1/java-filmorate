package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserStorage {


    User create(User user);

    User update(User user);

    void remove(Long id);

    User get(Long id);

    List<User> getAll();

}