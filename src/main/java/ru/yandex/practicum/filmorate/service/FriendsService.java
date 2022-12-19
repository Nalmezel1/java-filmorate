package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FriendsStorage;

import java.util.List;

@Service
public class FriendsService {

    private final FriendsStorage friendsStorage;

    @Autowired
    public FriendsService(FriendsStorage friendsStorage) {
        this.friendsStorage = friendsStorage;
    }

    //Создать дружбу
    public void create(Long userId, Long friendId) {
        friendsStorage.createFriend(userId, friendId);
    };

    //Удалить дружбу между пользователями
    public void remove(Long userId, Long friendId) {
        friendsStorage.deleteFriend(userId, friendId);
    };

    //Получить друзей пользователя
    List<User> getFriends(Long id) {
        return friendsStorage.getFriendsOfUser(id);
    };

    //Получить общих друзей пользователя
    List<User> getCommonsFriend(long userId, long friendId) {
        return friendsStorage.getCommonsFriend(userId, friendId);
    };
}
