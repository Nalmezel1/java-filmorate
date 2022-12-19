package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.DbStorage.FilmsGenresDbStorage;

import java.util.List;
import java.util.Map;

@Service
public class FilmsGenresServiсe {

    private final FilmsGenresDbStorage filmsGenresDbStorage;

    public FilmsGenresServiсe(FilmsGenresDbStorage filmsGenresDbStorage) {
        this.filmsGenresDbStorage = filmsGenresDbStorage;
    }
    //Получить все жанры заданных фильмов
    public List<Film> getGenresForGivenIds(String idFilms, Map<Long, Film> films) {
        return filmsGenresDbStorage.getGenres(idFilms, films);
    }
}
