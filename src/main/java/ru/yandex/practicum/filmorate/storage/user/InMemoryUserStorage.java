package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    Map<Long, User> storage = new HashMap<>();

    private long currentMaxId = 0L;

    public User create(User user) {
        currentMaxId++;
        user.setId(currentMaxId);

        storage.put(user.getId(), user);

        log.info(String.format("Элемент создан", user.getId()));
        log.debug(String.format("info = %s", user.toString()));
        return user;
    }


    public User update(User user) {

        storage.replace(user.getId(), user);

        log.info(String.format("Элемент обновлен", user.getId()));
        log.debug(String.format("info = %s", user.toString()));
        return user;
    }


    public void remove(Long id) {
        storage.remove(id);
    }


    public User get(Long id) {
        return storage.get(id);
    }


    public List<User> getAll() {
        return new ArrayList<>(storage.values());
    }
}
