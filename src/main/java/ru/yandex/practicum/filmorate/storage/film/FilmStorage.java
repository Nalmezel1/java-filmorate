package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public interface FilmStorage {

        Film create(Film film);
        Film update(Film film);
        void remove(Long id);
        Film get(Long id);
        List<Film> getAll();
}
