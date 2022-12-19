package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class FriendsDbStorage implements FriendsStorage {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createFriend(Long userId, Long friendId) {
        String sql = "INSERT INTO friends (user_id, friend_id)" +
                "VALUES (?, ?)";

        jdbcTemplate.update(sql, userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id=?";
        jdbcTemplate.update(sql, userId, friendId);

    }

    @Override
    public List<User> getFriendsOfUser(Long id) {
        String sql = "SELECT u.user_id, u.email, u.login, u.name, u.birthday " +
                "FROM friends AS f " +
                "JOIN  users AS u ON f.friend_id = u.user_id " +
                "WHERE f.user_id = ?;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseFriend(rs, rowNum), id);

    }

    @Override
    public List<User> getCommonsFriend(long userId, long friendId) {
        String sql = "SELECT u.* FROM users as u " +
                "JOIN (SELECT f.friend_id  FROM friends AS f " +
                " JOIN (SELECT f.friend_id AS id FROM friends AS f WHERE f.user_id = ?) " +
                "AS fr ON f.friend_id = fr.id WHERE f.user_id = ?) " +
                "AS fr ON fr.friend_id = u.user_id;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseFriend(rs, rowNum), userId, friendId);

    }


    private User parseFriend(ResultSet resultSet, int row) throws SQLException {
        return User.builder()
                .id(resultSet.getLong("user_id"))
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .build();
    }
}
