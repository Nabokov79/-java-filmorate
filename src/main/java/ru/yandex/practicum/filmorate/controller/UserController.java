package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        if (userStorage.getUserById(user.getId()) != null) {
            log.info("Ошибка добавления данных пользователя: " + user.getName());
            return ResponseEntity.badRequest().body(user);
        }
        userService.createUser(user);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (userStorage.getUserById(user.getId()) == null) {
            log.info("Ошибка обновления данных пользователя: " + user.getName());
            return ResponseEntity.notFound().build();
        }
        userService.updateUser(user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        if (userStorage.getUserById(id) == null ) {
            log.info("Ошибка параметра запроса: " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        if (userStorage.getUserById(id) == null ) {
            log.info("Ошибка параметра запроса: " + id);
            return ResponseEntity.notFound().build();
        }
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriends(@PathVariable long id, @PathVariable long friendId) {
        if (userStorage.getUserById(id) == null  || userStorage.getUserById(friendId) == null) {
            log.info("Ошибка параметра запроса: " + id + " " + friendId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userService.addNewFriend(id,friendId));
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriends(@PathVariable long id,@PathVariable long friendId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(friendId) == null) {
            log.info("Ошибка параметра запроса: " + id + " " + friendId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userService.deleteFriend(id,friendId));
    }

    @GetMapping(value = "/{id}/friends")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable long id) {
        if (userStorage.getUserById(id) == null || id < 0) {
            log.info("Ошибка параметра запроса: " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userService.getListUserFriends(id));
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getFriendsList(@PathVariable long id, @PathVariable long otherId) {
        if (userStorage.getUserById(id) == null || userStorage.getUserById(otherId) == null) {
            log.info("Ошибка параметра запроса: " + id + " " + otherId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(userService.getListOfMutualFriends(id, otherId));
    }
}