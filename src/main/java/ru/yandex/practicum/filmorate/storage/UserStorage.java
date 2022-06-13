package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;


public interface UserStorage {

    void createUser(User user);
    void updateUser(User user);
    User getUserById(long id);
    ArrayList<User> getAllUsers();
}