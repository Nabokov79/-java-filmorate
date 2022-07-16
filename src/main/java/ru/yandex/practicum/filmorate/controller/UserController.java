package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> saveUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(userService.saveUser(user));
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok().body(userService.getAllUsers());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<User> getUserById(@PathVariable long id) {
        return ResponseEntity.ok().body(userService.getUserById(id));
    }
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<User> deleteUserById(@PathVariable long id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> addFriends(@PathVariable long id, @PathVariable long friendId) {
        userService.addNewFriend(id,friendId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/friends/{friendId}")
    public ResponseEntity<User> deleteFriends(@PathVariable long id,@PathVariable long friendId) {
        userService.deleteFriend(id, friendId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/friends")
    public ResponseEntity<List<User>> getFriendsUserList(@PathVariable long id) {
        return ResponseEntity.ok().body(userService.getListUserFriends(id));
    }

    @GetMapping(value = "/{id}/friends/common/{otherId}")
    public ResponseEntity<List<User>> getListOfMutualFriends(@PathVariable long id, @PathVariable long otherId) {
        return ResponseEntity.ok().body(userService.getMutualFriends(id, otherId));
    }
}