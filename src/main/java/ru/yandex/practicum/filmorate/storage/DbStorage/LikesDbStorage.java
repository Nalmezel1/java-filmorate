package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Likes;
import ru.yandex.practicum.filmorate.storage.LikesStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
@Component
public class LikesDbStorage implements LikesStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public LikesDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        String sqlQuery = "INSERT INTO USERS_LIKES (film_id, user_id) VALUES (?, ?);";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public Collection<Likes> getAllLikes(Long filmId) {
        String sql = "SELECT * FROM USERS_LIKES WHERE film_id = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseLike(rs, rowNum), filmId);

    }

    @Override
    public void removeLike(Long filmId, Long userId) {
        likeCheck(filmId, userId);
        String sqlQuery = "DELETE FROM USERS_LIKES WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(sqlQuery, filmId, userId);

    }

    private void likeCheck(Long filmId, Long userId){
        SqlRowSet users = jdbcTemplate.queryForRowSet("SELECT * FROM USERS_LIKES WHERE user_id = ?;", userId);
        SqlRowSet films = jdbcTemplate.queryForRowSet("SELECT * FROM USERS_LIKES WHERE film_id = ?;", filmId);

        if(!users.next() || !films.next()){
            throw new NotFoundException("еверный id");
        }
    }

    private Likes parseLike(ResultSet resultSet, int rowNum) throws SQLException {
        return Likes.builder()
                .likeId(resultSet.getInt("users_likes_id"))
                .filmId(resultSet.getInt("film_id"))
                .userId(resultSet.getInt("user_id"))
                .build();
    }
}
