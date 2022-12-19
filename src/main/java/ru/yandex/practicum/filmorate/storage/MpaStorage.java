package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface MpaStorage {
    Mpa getMpa(long id);
    Collection<Mpa> getAll();

    boolean isMpaExist(int id);
}
