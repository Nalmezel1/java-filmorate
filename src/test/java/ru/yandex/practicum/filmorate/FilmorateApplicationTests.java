package ru.yandex.practicum.filmorate;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {



	@Autowired
	private static User user;
	private static Film film;
	UserStorage userStorage = new InMemoryUserStorage();
	FilmStorage filmStorage = new InMemoryFilmStorage();
    FilmService filmService = new FilmService(filmStorage,userStorage);
    UserService userService = new UserService(userStorage);
	UserController userController = new UserController(userService);
	FilmController filmController = new FilmController(filmService);

	@BeforeEach
	void setUp() {
		user = new User();
		user.setId(1L);
		user.setName("name");
		user.setLogin("login");
		user.setEmail("test@test.ru");
		user.setBirthday(LocalDate.of(2000, 1, 1));

		film = new Film();
		film.setId(1L);
		film.setName("name");
		film.setDescription("description");
		film.setReleaseDate(LocalDate.of(2000, 1, 1));
		film.setDuration(100);
	}

	@Test
	public void correctUserTest()throws ValidationException{
		User user1 = userController.create(user);
		assertEquals(1L, user1.getId(), "Не верное id");
		assertEquals("name", user1.getName(), "Не верное имя");
		assertEquals("login", user1.getLogin(), "Не верное login");
		assertEquals("test@test.ru", user1.getEmail(), "Не верное email");
		assertEquals(LocalDate.of(2000, 1, 1), user1.getBirthday(), "Не верное др");
	}
	@Test
	public void correctFilmTest()throws ValidationException{
		Film film1 = filmController.create(film);
		assertEquals(1L, film1.getId(), "Не верное id");
		assertEquals("name", film1.getName(), "Не верное название");
		assertEquals("description", film1.getDescription(), "Не верное description");
		assertEquals(100, film1.getDuration(), "Не верное Duration");
		assertEquals(LocalDate.of(2000, 1, 1), film1.getReleaseDate(), "Не верное ReleaseDate");
	}

	@Test
	void emptyNameTest() throws ValidationException {
		user.setName("");
		User user1 = userController.create(user);
		assertEquals("login", user1.getName(), "Если не задано имя, то должен браться логин");
		user.setName(null);
		User user2 = userController.create(user);
		assertEquals("login", user2.getName(), "Если не задано имя, то должен браться логин");
	}

	@Test
	void incorrectEmailTest()  {
		user.setEmail("");
		final ValidationException exception1 = assertThrows(

				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user1 = userController.create(user);
					}
				});
		assertEquals("Не корректный email", exception1.getMessage());
		user.setEmail("test.ru");
		final ValidationException exception2 = assertThrows(

				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						User user1 = userController.create(user);
					}
				});
		assertEquals("Не корректный email", exception2.getMessage());
	}
	@Test
	public void incorrectFilmReleaseDayTest() {
		film.setReleaseDate(LocalDate.of(1895, 12, 27));
		final ValidationException exception = assertThrows(

				ValidationException.class,
				new Executable() {
					@Override
					public void execute() throws ValidationException {
						Film film1 = filmController.create(film);
					}
				});
		assertEquals("Неверная дата релиза", exception.getMessage());
	}
}


