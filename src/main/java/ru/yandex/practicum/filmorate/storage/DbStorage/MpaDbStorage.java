package ru.yandex.practicum.filmorate.storage.DbStorage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.IdNegativeException;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component
public class MpaDbStorage implements MpaStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpa(long id) {

        String sql = "SELECT mpa_id, rating_name FROM mpa_rating WHERE mpa_id = ?;";

        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> parseMpa(rs, rowNum), id);
    }


    @Override
    public Collection<Mpa> getAll() {
        String sql = "SELECT mpa_id, rating_name FROM mpa_rating;";
        return jdbcTemplate.query(sql, (rs, rowNum) -> parseMpa(rs, rowNum));

    }

    @Override
    public boolean isMpaExist(int id) {
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet("SELECT * FROM mpa_rating WHERE mpa_id = ?;", id);

        if (!mpaRows.next()){
            throw new NotFoundException("еверный id");
        }
        return mpaRows.next();
    }

    private Mpa parseMpa(ResultSet resultSet, int rowNum) throws SQLException {

        return Mpa.builder()
                .id(resultSet.getLong("mpa_id"))
                .name(resultSet.getString("rating_name"))
                .build();
    }
}
