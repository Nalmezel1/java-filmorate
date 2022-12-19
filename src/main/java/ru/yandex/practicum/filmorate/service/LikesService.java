package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.storage.DbStorage.LikesDbStorage;

@Service
public class LikesService {
    private final LikesDbStorage likesDbStorage;

    @Autowired
    public LikesService(LikesDbStorage likesDbStorage) {
        this.likesDbStorage = likesDbStorage;
    }

    public void addLike(Long filmId, Long userId) {
        likesDbStorage.addLike(filmId, userId);
    }

    public void removeLike(Long filmId, Long userId) {
        likesDbStorage.removeLike(filmId, userId);
    }
}