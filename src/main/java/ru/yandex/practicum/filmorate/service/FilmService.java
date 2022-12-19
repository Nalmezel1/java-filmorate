package ru.yandex.practicum.filmorate.service;


import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.LikesStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    final LocalDate MIN_RELEASE_DAY=LocalDate.of(1895,12,28);
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserService userService;
    private final GenreService genreService;
    private final FilmsGenresServiсe filmsGenresServiсe;

    private final LikesStorage likesStorage;


    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage, @Qualifier("userDbStorage") UserStorage userStorage, UserService userService, GenreService genreService, FilmsGenresServiсe filmsGenresServiсe, LikesStorage likesStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.userService = userService;
        this.genreService = genreService;
        this.filmsGenresServiсe = filmsGenresServiсe;
        this.likesStorage = likesStorage;
    }

    public Film createFilm (Film film)  {
        validate(film);
        Film newfilm = filmStorage.create((film));
        genreService.writeFilmGenres(newfilm.getId(),newfilm.getGenres());

        return newfilm;
    }

    public Film updateFilm (Film film) {
        if (filmStorage.isFilmExist(film.getId())) {
            validate(film);
            filmStorage.update(film);
            genreService.addFilmGenres(film.getId(), film.getGenres());
            return getFilm(film.getId());
        } else {
            throw new NotFoundException("фильм с id" + film.getId() + " не существует");
        }
    }

    public void deleteFilm (long  id) {
        if (filmStorage.isFilmExist(id)) {
            filmStorage.remove(id);
            genreService.deleteFilmGenres(id);
        } else {
            throw new NotFoundException("фильм с id" + id + " не существует");
        }
    }

    public Film getFilm (long id){
        if (filmStorage.isFilmExist(id)) {
            Film film = filmStorage.get(id);
            List<Genre> genres = genreService.getFilmGenres(film.getId());
            if (genres.size() > 0) {
                film.setGenres(genres);
            }
            return film;
        } else {
            throw new NotFoundException("фильм с id" + id + " не существует");
        }
    }

    public List<Film> getAllFilms(){
        return filmStorage.getAll();
    }

    public List<Film> getPopular(int count) {

        return filmStorage.getPopularFilms(count);

   }


    @SneakyThrows
    public void validate(Film film)  {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DAY)){
            throw new ValidationException("Неверная дата релиза");
        }
    }


}
