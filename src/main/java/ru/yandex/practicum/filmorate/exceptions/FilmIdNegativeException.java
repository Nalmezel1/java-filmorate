package ru.yandex.practicum.filmorate.exceptions;

public class FilmIdNegativeException extends RuntimeException {
    public FilmIdNegativeException(String message) {
        super(message);
    }
}