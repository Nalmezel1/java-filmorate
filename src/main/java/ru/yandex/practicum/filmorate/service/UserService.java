package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    private final FriendsService friendsService;
    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage, FriendsService friendsService) {
        this.userStorage = userStorage;
        this.friendsService = friendsService;
    }

    public User createUser (User user) throws ValidationException {


        System.out.println(user);


        validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (userStorage.isExistInStorage(user.getId())) {
            return userStorage.update(user);
        } else {
            throw new NotFoundException("Пользователь с  id " + user.getId() + " не существует");
        }
    }
    public void deleteUser(long id) {
        if (userStorage.isExistInStorage(id)) {
            userStorage.remove(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + "  не существует");
        }
    }

    public User getUser (long id){
        if (userStorage.isExistInStorage(id)) {
            return userStorage.get(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + "  не существует");
        }
    }
    public List<User> getAllUsers(){
        return userStorage.getAll();
    }


    public void addFriend(Long userId, Long friendId) {
        if (userStorage.isExistInStorage(userId) && userStorage.isExistInStorage(friendId)) {
            friendsService.create(userId, friendId);
        } else {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        if (userStorage.isExistInStorage(userId) && userStorage.isExistInStorage(friendId)) {
            friendsService.remove(userId, friendId);
        }else {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    public List<User> getFriends(Long userId) {
        if (userStorage.isExistInStorage(userId)) {
            return friendsService.getFriends(userId);
        }
        throw new NotFoundException(userId + " Такого пользователя нет");
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {

        if(userStorage.isExistInStorage(userId) && userStorage.isExistInStorage(otherUserId)){
            return friendsService.getCommonsFriend(userId, otherUserId);
        }
        throw new NotFoundException(userId + " Такого пользователя нет");

    }

    public void validate(User user) throws ValidationException {
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn(String.format("Name пустой, присваивается занчение login: %s", user.getLogin()));
            user.setName(user.getLogin());
        }
        if (!user.getEmail().contains("@") || user.getEmail().isEmpty()) {
            throw new ValidationException("Не корректный email");
        }

    }

}
