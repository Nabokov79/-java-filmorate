package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import java.util.*;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void createUser(User user) {
        userStorage.createUser(user);
    }

    public void updateUser(User user) {
        userStorage.updateUser(user);
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(long id) {
        return userStorage.getUserById(id);
    }
    public void deleteUserById(long id) {
        userStorage.deleteUserById(id);
    }

    public User addNewFriend(long id, long friendId) {
        User user = userStorage.getUserById(id);
        user.addFriend(friendId);
        userStorage.updateUser(user);
        User friend = userStorage.getUserById(friendId);
        friend.addFriend(id);
        userStorage.updateUser(friend);
        return user;
    }

    public User deleteFriend(long id, long friendId) {
        userStorage.getUserById(id).deleteFriend(friendId);
        return userStorage.getUserById(id);

    }

    public List<User> getListUserFriends(long id) {
        User user = userStorage.getUserById(id);
        List<User> userFriends = new ArrayList<>();
        for (Long friendId : user.getFriends()) {
            userFriends.add(userStorage.getUserById(friendId));
        }
        return userFriends;
    }

    public List<User> getListOfMutualFriends(long id, long otherId) {
        List<User> listMutualFriends = new ArrayList<>();
        for (Long firstId : userStorage.getUserById(id).getFriends()) {
            for (Long secondId :userStorage.getUserById(otherId).getFriends()) {
                if (Objects.equals(firstId, secondId)) {
                    listMutualFriends.add(userStorage.getUserById(firstId));
                }
            }
        }
        return listMutualFriends;
    }
}