package ru.yandex.practicum.filmorate.controllers;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.List;


@RestController
@Slf4j
@Validated
@RequestMapping("/films")
public class FilmController {



    private final FilmService filmService;
    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film newFilm) throws ValidationException {
        return filmService.createFilm(newFilm);
    }

    @GetMapping
    public List<Film> getAll() {
        return filmService.getAllFilms();
    }
    @GetMapping("/{id}")
    public Film getById(@PathVariable Long id) {
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@DefaultValue("10") @Positive @RequestParam
            (value = "count", defaultValue = "10 ", required = false) Integer count) {
        return filmService.getPopular(count);
    }




    @PutMapping
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public  void addLike(@PathVariable Long id, @PathVariable Long userId) throws ValidationException {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public  void delLike(@PathVariable Long id, @PathVariable Long userId) throws ValidationException {
        filmService.removeLike(id, userId);
    }

}
