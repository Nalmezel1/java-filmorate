package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.exceptions.IdNegativeException;

import java.util.*;

@Service
public class GenreService {
    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAll() {
        return genreStorage.getAllGenres();
    }


    public List<Genre> getFilmGenres(Long filmId) {
        return genreStorage.getFilmGenres(filmId);
    }


    public Genre getGenre(long id) {
        checkGenreId(id);
        return genreStorage.getGenre(id);
    }

    public Collection<Genre> addFilmGenres(Long filmId, List<Genre> genres) {
        return genreStorage.addFilmGenres(filmId, genres);
    }

    public void deleteFilmGenres(Long filmId) {
        genreStorage.deleteFilmGenres(filmId);
    }


    public Map<Long, List<Genre>> addGenreToFilm(List<Long> idFilm) {
        Map<Long, List<Genre>> genres = new HashMap<>();

        for(long id : idFilm) {
            if (checkGenres(id)) {
                genres.put(id, getFilmGenres(id)) ;
            } else {
                genres.put(id, new ArrayList<Genre>());
            }
        }
        return genres;
    }


    public void writeFilmGenres(Long filmId, List<Genre> genres) {
        deleteFilmGenres(filmId);
        if (genres != null && genres.size() > 0) {
            for (int i = 0; i < genres.size(); i++)
                genreStorage.writeFilmGenres(filmId, genres.get(i).getId());
        }
    }

    private boolean checkGenres(long id) {
        return genreStorage.isGenreExist(id);
    }

    private void checkGenreId(long id) {

        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        System.out.println(id);
        System.out.println(checkGenres(id));
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");

        if (id < 0 || checkGenres(id)) {
            throw new IdNegativeException("нверный id");
        }
    }

}
