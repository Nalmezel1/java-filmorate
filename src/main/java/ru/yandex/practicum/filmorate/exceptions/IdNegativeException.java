package ru.yandex.practicum.filmorate.exceptions;

public class IdNegativeException extends RuntimeException {
    public IdNegativeException(String message) {
        super(message);
    }
}