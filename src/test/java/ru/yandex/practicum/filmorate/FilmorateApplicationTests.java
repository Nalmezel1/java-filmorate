package ru.yandex.practicum.filmorate;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.nio.file.FileStore;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmorateApplicationTests {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private final FilmStorage filmStorage;
	private final GenreStorage genreStorage;
	private final LikesStorage likesStorage;
	private final MpaStorage mpaStorage;
	private final UserStorage userStorage;
	private final FriendsStorage friendsStorage;

	private User createUser() {
		User user = new User();
		user.setEmail("email@email.ru");
		user.setLogin("login");
		user.setName("user");
		user.setBirthday(LocalDate.of(2022, 1, 1));
		System.out.println(userStorage.create(user));
		return userStorage.create(user);
	}

	private Film createFilm() {
		Film film = new Film();
		film.setName("film");
		film.setDescription("desc");
		film.setReleaseDate(LocalDate.of(2022, 1, 1));
		film.setDuration(100);
		film.setMpa(mpaStorage.getMpa(1L));
		film.setGenres(List.of(genreStorage.getGenre(1L)));
		return filmStorage.create(film);
	}

	@AfterEach
	public void execute() {
		JdbcTestUtils.deleteFromTables(jdbcTemplate,
				"FILMS_GENRES",
				"FRIENDS",
				"USERS_LIKES",
				"USERS",
				"FILMS"
		);
	}
	@Test
	public void getAllMpasAndGenres() {
		assertEquals(mpaStorage.getAll().size(), 5);
		assertEquals(genreStorage.getAllGenres().size(), 6);
	}

	@Test
	public void findFilms() {
		createFilm();
		createFilm();
		long id = createFilm().getId();
		Film film = filmStorage.get(id);
		assertEquals(film.getName(), "film");
		assertEquals(filmStorage.getAll().size(), 3);
	}


}