package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class FilmDbStorage  implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    @Override
    public Film create(Film film) {

        String sql = "INSERT INTO films (film_name, description, release_date, duration, mpa_id,rate) " +
                "VALUES (?, ?, ?, ?, ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"film_id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setLong(5, film.getMpa().getId());
            statement.setInt(6, film.getRate());
            return statement;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return film;
    }

    @Override
    public Film update(Film film) {

        String sql1 = "UPDATE films " +
                "SET film_name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ? , rate = ? " +
                "WHERE film_id = ?;";
        jdbcTemplate.update(sql1, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getMpa().getId(), film.getRate(), film.getId());

        String sql2 = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?;";
        jdbcTemplate.update(sql2, film.getId());

        if (film.getGenres() != null) {
            String sql3 = "SELECT DISTINCT g.genre_id, g.name_genre " +
                    "FROM genres AS g " +
                    "RIGHT JOIN films_genres AS fg ON g.genre_id = fg.genres_id " +
                    "WHERE fg.film_id = ?;";
            List<Integer> genresIds = jdbcTemplate.query(sql3,
                    this::parseGenreIds, film.getId());

            film.getGenres().stream()
                    .map(Genre::getId)
                    .filter(id -> !genresIds.contains(id))
                    .collect(Collectors.toSet())
                    .forEach(genreId -> {
                        jdbcTemplate.update("INSERT INTO FILMS_GENRES(film_id, genres_id)\n" +
                                "VALUES (?, ?)", film.getId(), genreId);
                    });
        }
        return get(film.getId());
    }
    private Integer parseGenreIds(ResultSet rs, int rowNum) throws SQLException {
        return rs.getInt("genre_id");
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM films WHERE film_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public Film get(Long id) {
        String sql =
                "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration, f.mpa_id, m.rating_name " +
                        "FROM films AS f " +
                        "JOIN mpa_rating AS m ON m.mpa_id = f.mpa_id " +
                        "WHERE f.film_id = ?;";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseFilm(rs, rowNum), id);
    }

    @Override
    public List<Film> getAll() {
        String sql =
                "SELECT f.film_id, f.film_name, f.description, f.release_date, f.duration,f.rate, f.Mpa_id, m.rating_name, fg.genres_id, g.name_genre  " +
                        "FROM films f " +
                        "LEFT JOIN mpa_rating AS m ON m.mpa_id = f.Mpa_id " +
                        "LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                        "LEFT JOIN genres AS g ON g.genre_id = fg.genres_id " +
                        "GROUP BY f.film_id;";

        return jdbcTemplate.query(sql,(rs, rowNum) -> parseFilm(rs, rowNum));

    }

    @Override
    public List<Film> getPopularFilms(int count) {
        String sqlQuery =
                "SELECT f.film_id, f.film_name, f.description,f.rate + COUNT(l.film_id) as rating, f.release_date, f.duration, f.Mpa_id, m.rating_name , " +
                        "FROM films AS f " +
                        "LEFT JOIN mpa_rating AS m ON m.mpa_id = f.mpa_id " +
                        "LEFT JOIN users_likes AS l ON l.film_id = f.film_id " +
                        "LEFT JOIN films_genres AS fg ON fg.film_id = f.film_id " +
                        "LEFT JOIN genres AS g ON g.genre_id = fg.genres_id " +
                        "GROUP BY f.film_id "+
                        "ORDER BY rating DESC "+
                        "LIMIT ?;";

        return jdbcTemplate.query(sqlQuery, (rs, rowNum) -> parseFilm(rs, rowNum), count);
    }



    private Film parseFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = Mpa.builder()
                .id(resultSet.getInt("mpa_id"))
                .name(resultSet.getString("rating_name"))
                .build();

        return Film.builder()
                .id(resultSet.getLong("film_id"))
                .name(resultSet.getString("film_name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(mpa)
                .genres(getFilmGenres(resultSet.getLong("film_id")))
                .build();
    }
    public List<Genre> getFilmGenres(Long filmId) {
        String sql = "SELECT DISTINCT g.genre_id, g.name_genre " +
                "FROM genres AS g " +
                "RIGHT JOIN films_genres AS fg ON g.genre_id = fg.genres_id " +
                "WHERE fg.film_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseGenre(rs, rowNum), filmId);
    }
    private Genre parseGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getLong("genre_id"))
                .name(resultSet.getString("name_genre"))
                .build();
    }

    @Override
    public boolean isFilmExist(Long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("SELECT * FROM films WHERE film_id = ?;", id);
        if (filmRows.next()) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public boolean iiLikeExist(Long filmId, Long userId) {
        String sqlQuery = "SELECT COUNT(user_id) " +
                "FROM users_likes " +
                "WHERE film_id = ? AND user_id = ?;";
        int like = jdbcTemplate.queryForObject(sqlQuery, Integer.class, filmId, userId);
        return 0 < like;
    }

}
