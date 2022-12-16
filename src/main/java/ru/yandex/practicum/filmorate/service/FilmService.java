package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    final LocalDate MIN_RELEASE_DAY=LocalDate.of(1895,12,28);
    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film createFilm (Film film) throws ValidationException {
        validate(film);
        return filmStorage.create(film);
    }

    public Film updateFilm (Film film) throws ValidationException {
        if (isFilmExistInStorage(film.getId())) {
            validate(film);
            return filmStorage.update(film);
        } else {
            throw new NotFoundException("фильм с id" + film.getId() + " не существует");
        }
    }

    public void deleteFilm (long  id) {
        if (isFilmExistInStorage(id)) {
            filmStorage.remove(id);
        } else {
            throw new NotFoundException("фильм с id" + id + " не существует");
        }
    }

    public Film getFilm (long id){
        if (isFilmExistInStorage(id)) {
            return filmStorage.get(id);
        } else {
            throw new NotFoundException("фильм с id" + id + " не существует");
        }
    }

    public List<Film> getAllFilms(){
        return filmStorage.getAll();
    }

    public void addLike(Long filmId, Long userId) throws ValidationException {
        Film film = getFilm(filmId);
        if(isUserExistInStorage(userId)){
            film.addLike(userId);
            updateFilm(film);
        }else{
            throw new NotFoundException("Этот пользователь не существует");
        }
    }

    public void removeLike(Long filmId, Long userId) throws NotFoundException, ValidationException {
        Film film = getFilm(filmId);
        if (!film.getLikes().contains(userId)) {
            throw new NotFoundException("Этот пользователь не лайкал фильм");
        }
        film.removeLike(userId);
        updateFilm(film);
    }


    public List<Film> getPopular(int count) {
        List<Film> sortedListForFilms = filmStorage.getAll().stream()
                .sorted(Comparator.comparing(x->-x.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        return sortedListForFilms;
    }


    public void validate(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(MIN_RELEASE_DAY)){
            throw new ValidationException
                    (String.format("Неверная дата релиза", film.getId()));
        }
    }

    public boolean isFilmExistInStorage(long id) {
        List<Film> allFilms = filmStorage.getAll();
        if (allFilms.stream().anyMatch(s->s.getId() == id)) {

            return true;
        } else {
            return false;
        }
    }
    public boolean isUserExistInStorage(long id) {
        List<User> allUsers = userStorage.getAll();
        if (allUsers.stream().anyMatch(s->s.getId() == id)) {
            return true;
        } else {
            return false;
        }
    }
}
