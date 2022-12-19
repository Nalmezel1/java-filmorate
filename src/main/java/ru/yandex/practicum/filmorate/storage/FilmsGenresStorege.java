package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;

public interface FilmsGenresStorege {

    List<Film> getGenres(String filmId, Map<Long, Film> films);

}
