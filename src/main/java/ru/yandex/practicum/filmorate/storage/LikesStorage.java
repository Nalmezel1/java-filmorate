package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Likes;

import java.util.Collection;

public interface LikesStorage {
    void addLike(Long filmId, Long userId);
    Collection<Likes> getAllLikes(Long filmId);
    void removeLike(Long filmId, Long userId);
}
