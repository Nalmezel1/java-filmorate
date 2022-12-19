package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    List<Genre> getFilmGenres(Long filmId);
    Genre getGenre(long id);
    Collection<Genre> getAllGenres();
    void deleteFilmGenres(Long filmId);
    boolean isGenreExist(long id);
    void writeFilmGenres(Long filmId, Long genresId);

    void isGenreHaveFilms(long id);

    Collection<Genre> addFilmGenres(Long filmId, List<Genre> genres);

}
