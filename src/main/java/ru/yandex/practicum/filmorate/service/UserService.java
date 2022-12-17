package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;
    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User createUser (User user) throws ValidationException {
        validate(user);
        return userStorage.create(user);
    }

    public User updateUser(User user) {
        if (isUserExistInStorage(user.getId())) {
            return userStorage.update(user);
        } else {
            throw new NotFoundException("Пользователь с  id " + user.getId() + " не существует");
        }
    }
    public void deleteUser(long id) {
        if (isUserExistInStorage(id)) {
            userStorage.remove(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + "  не существует");
        }
    }

    public User getUser (long id){
        if (isUserExistInStorage(id)) {
            return userStorage.get(id);
        } else {
            throw new NotFoundException("Пользователь с id " + id + "  не существует");
        }
    }
    public List<User> getAllUsers(){
        return userStorage.getAll();
    }


    public void addFriend(Long userId, Long friendId) {
        if (isUserExistInStorage(userId) && isUserExistInStorage(friendId)) {
            User user = getUser(userId);
            user.addFriend(friendId);
            updateUser(user);
            User friend = getUser(friendId);
            friend.addFriend(userId);
            updateUser(friend);
        } else {
            throw new NotFoundException("Такого пользователя нет");
        }
    }

    public void removeFriend(Long userId, Long friendId) {
        if (isUserExistInStorage(userId) && isUserExistInStorage(friendId)) {
            User user = getUser(userId);
            user.removeFriend(friendId);
            updateUser(user);
            User friend = getUser(friendId);
            friend.removeFriend(userId);
            updateUser(friend);

        }
    }

    public List<User> getFriends(Long userId) {
        if (isUserExistInStorage(userId)) {
            User user = getUser(userId);
            return user.getFriends().stream().map(userStorage::get).collect(Collectors.toList());
        }
        throw new NotFoundException(userId + " Такого пользователя нет");
    }

    public List<User> getCommonFriends(Long userId, Long otherUserId) {
        List<User> result = new ArrayList<>();
        Set<Long> otherFriends = userStorage.get(otherUserId).getFriends();
        for (Long friendId : userStorage.get(userId).getFriends()) {
            if (otherFriends.contains(friendId)) {
                result.add(userStorage.get(friendId));
            }
        }
        return result;
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


    public boolean isUserExistInStorage(long id) {
        List<User> allUsers = userStorage.getAll();
        if (allUsers.stream().anyMatch(s->s.getId() == id)) {
            return true;
        } else {
            return false;
        }
    }
}
