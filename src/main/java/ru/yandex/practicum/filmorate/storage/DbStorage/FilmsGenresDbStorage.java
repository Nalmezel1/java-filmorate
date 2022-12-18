package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsGenres;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmsGenresStorege;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class FilmsGenresDbStorage implements FilmsGenresStorege {
    private final JdbcTemplate jdbcTemplate;
    public FilmsGenresDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getGenres(String filmId, Map<Long, Film> films) {
        String sql = "SELECT g.genre_id, g.name_genre, fg.film_id " +
                "FROM genres AS g " +
                "RIGHT JOIN films_genres AS fg ON g.genre_id = fg.genres_id " +
                "WHERE fg.film_id IN ( " + filmId + " );";
        List<FilmsGenres> filmsGenres = jdbcTemplate.query(sql, (rs, rowNum) -> parseGenre(rs, rowNum)
        );
        for (FilmsGenres filmsGenre : filmsGenres) {
            films.get(filmsGenre.getIdFilm()).getGenres().add(new Genre(filmsGenre.getGenreId(),
                    filmsGenre.getName()));
        }
        List <Film> filmList = new ArrayList<>();
        for(Map.Entry<Long, Film> entry : films.entrySet()) {
            filmList.add(entry.getValue());
        }
        return filmList;
    }

    public FilmsGenres parseGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return FilmsGenres.builder()
                .idFilm(resultSet.getInt("film_id"))
                .genreId(resultSet.getInt("genre_id"))
                .name(resultSet.getString("name_genre"))
                .build();
    }
}
