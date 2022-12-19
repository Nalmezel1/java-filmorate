package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    //Получ
    @Override
    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT DISTINCT g.genre_id, g.name_genre " +
                "FROM genres AS g " +
                "RIGHT JOIN films_genres AS fg ON g.genre_id = fg.genres_id " +
                "WHERE fg.film_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseGenre(rs, rowNum), filmId);
    }

    @Override
    public Genre getGenre(long id) {
        String sql = "SELECT genre_id, name_genre FROM genres WHERE genre_id = ?;";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseGenre(rs, rowNum), id);
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sql = "SELECT genre_id, name_genre FROM genres;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseGenre(rs, rowNum));

    }


    @Override
    public void deleteFilmGenres(Long filmId) {
        String sql = "DELETE FROM films_genres WHERE film_id = ?;";
        jdbcTemplate.update(sql, filmId);
    }


    public boolean checkGenreOfFilm(Long filmId, Long genreId) {
        String sql = "SELECT COUNT(genres_id) FROM films_genres WHERE film_id = ? AND genres_id = ?;";
        int quantity = jdbcTemplate.queryForObject(sql, Integer.class, filmId, genreId);
        return quantity > 0;
    }

    @Override
    public void writeFilmGenres(Long filmId, Long genresId) {
        String sql = "INSERT INTO films_genres (film_id, genres_id) VALUES (?, ?);";
        jdbcTemplate.update(sql, filmId, genresId);
    }


    private Genre parseGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name_genre"))
                .build();
    }
    @Override
    public boolean isGenreExist(long id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM GENRES WHERE genre_id = ?;", id);

        if(!mpaRows.next()){
            throw new NotFoundException("еверный id");
        }
        return mpaRows.next();
    }

    @Override
    public void isGenreHaveFilms(long id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM FILMS_GENRES WHERE genres_id = ?;", id);
        if(!mpaRows.next()){
            throw new NotFoundException("еверный id");
        }

    }



    @Override
    public Collection<Genre> addFilmGenres(Long filmId, List<Genre> genres) {
        deleteFilmGenres(filmId);
        if(genres != null && genres.size() > 0) {
            for (int i = 0; i < genres.size(); i++) {
                if (!checkGenreOfFilm(filmId, genres.get(i).getId())) {
                    writeFilmGenres(filmId, genres.get(i).getId());
                }
            }
        }
        return genres;
    }

}
