package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;
import ru.yandex.practicum.filmorate.exceptions.IdNegativeException;

import java.util.Collection;

@Service
public class MpaService {
    private final MpaStorage mpaStorage;

    @Autowired
    public MpaService(MpaStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    public Mpa get(int id) {
        check(id);
        return mpaStorage.getMpa(id);
    }

    public Collection<Mpa> getAll() {
        return  mpaStorage.getAll();
    }

    private void check(int id) {
        if (id < 0 || mpaStorage.isMpaExist(id)) {
            throw new IdNegativeException("id < 0");
        }
    }
}
