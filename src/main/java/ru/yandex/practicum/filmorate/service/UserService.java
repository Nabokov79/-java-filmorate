package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.CustomException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User saveUser(User user) {
        userStorage.saveUser(user);
        return user;
    }

    public User updateUser(User user) {
        getUserById(user.getId());
        userStorage.updateUser(user);
        return getUserById(user.getId());
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        try {
            return userStorage.getUserById(id);
        } catch (DataAccessException dax) {
            log.info("Film id = " + id + " not found.");
            throw new CustomException("Film id = " + id + " not found.");
        }
    }
    public void deleteUserById(long id) {
        getUserById(id);
        userStorage.deleteUserById(id);
    }

    public void addNewFriend(long id, long friendId) {
        getUserById(id);
        getUserById(friendId);
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(long id, long friendId) {
        getUserById(id);
        getUserById(friendId);
        userStorage.deleteFriends(id, friendId);
    }

    public List<User> getListUserFriends(long id) {
        getUserById(id);
        return userStorage.getListUserFriends(id);
    }

    public List<User> getMutualFriends(long id, long otherId) {
       getUserById(id);
       getUserById(otherId);
        return userStorage.getListMutualFriends(id, otherId);
    }
}