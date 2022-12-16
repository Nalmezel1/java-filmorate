package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage{

    HashMap<Long, Film> storage = new HashMap<>();

    private long currentMaxId = 0L;


    @Override
    public Film create(Film film) {
        currentMaxId++;
        film.setId(currentMaxId);

        storage.put(film.getId(), film);

        log.info(String.format("Элемент создан", film.getId()));
        log.debug(String.format("info = %s", film.toString()));
        return film;
    }


    @Override
    public Film update(Film film){
        storage.replace(film.getId(), film);

        log.info(String.format("Элемент обновлен", film.getId()));
        log.debug(String.format("info = %s", film.toString()));
        return film;
    }

    @Override
    public void remove(Long id) {
        storage.remove(id);
    }

    @Override
    public Film get(Long id) {
        return storage.get(id);
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(storage.values());
    }


}
