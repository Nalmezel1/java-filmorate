package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {

        String sql = "INSERT INTO users (email, login, name, birthday) VALUES (?, ?, ?, ?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(sql, new String[]{"user_id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId(keyHolder.getKey().longValue());
        return user;
    }

    @Override
    public User update(User user) {

        String sql = "UPDATE USERS SET email = ?, login = ?, name = ?,  birthday = ? WHERE user_id = ?;";
        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return user;
    }

    @Override
    public void remove(Long id) {
        String sql = "DELETE FROM USERS WHERE user_id = ?;";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public User get(Long id) {
        String sql = "SELECT * FROM USERS WHERE user_id = ?;";
        return  jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseUsers(rs, rowNum), id);

    }

    @Override
    public List<User> getAll() {
        String sql = "SELECT * FROM USERS;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseUsers(rs, rowNum));
    }

    @Override
    public boolean isExistInStorage(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id = ?;", id);
        if (userRows.next()) {
            return true;
        } else {
            return false;
        }
    }

    private User parseUsers(ResultSet resultSet, int rowNum) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
