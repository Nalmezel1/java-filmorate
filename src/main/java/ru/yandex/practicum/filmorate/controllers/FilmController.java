package ru.yandex.practicum.filmorate.controllers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
* Вопрос, можно ли не писать доп. тесты, если есть те, что в postman?
* */
@Slf4j
@RestController
public class FilmController {
    public Map<Long, Film> films = new HashMap<>();
    long currentMaxId = 0L;
    private final LocalDate MIN_RELEASE_DAY=LocalDate.of(1895,12,28);

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {

        checkFilmReleaseDate(film);
            currentMaxId++;
            film.setId(currentMaxId);
            films.put(film.getId(), film);

            log.info(String.format("Фильм c id=%d обновлен", film.getId()));
            log.debug(String.format("info фильма = %s", film.toString()));
            return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (!films.containsKey(film.getId())) {
            throw new ValidationException(String.format("Фильм с id= %d не найден", film.getId()));
        }
        films.put(film.getId(), film);

        log.info(String.format("Фильм c id=%d обновлен", film.getId()));
        log.debug(String.format("info фильма = %s", film.toString()));
        return film;
    }

    @GetMapping("/films")
    public List<Film> listAllFilms() {
        return List.copyOf(films.values());
    }

    public void checkFilmReleaseDate (Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DAY)){

            throw new ValidationException
                    (String.format("Неверная дата релиза", film.getId()));
        }
    }
}
