package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {

    void createFriend(Long userId, Long friendId);
    void deleteFriend(Long userId, Long friendId);
    List<User> getFriendsOfUser(Long id);
    List<User> getCommonsFriend(long userId, long friendId);
}
