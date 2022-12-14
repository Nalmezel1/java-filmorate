package ru.yandex.practicum.filmorate.controllers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

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
@RequestMapping("/films")
public class FilmController extends AbstractController<Film>{

    private final LocalDate MIN_RELEASE_DAY=LocalDate.of(1895,12,28);
    @Override
    public void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DAY)){
            throw new ValidationException
                    (String.format("Неверная дата релиза", film.getId()));
        }
    }
}
