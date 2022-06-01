package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exeption.CustomException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    public long createId() {
        long id = 0;
        for(Long i : users.keySet()) {
            if (id < i) {
                id = i;
            }
        }
        return id + 1;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        user.setId(createId());
        if (users.containsKey(user.getId())) {
            log.info("Ошибка добавления данных пользователя: " + user.getName());
            return ResponseEntity.badRequest().body(user);
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        if (user.getId() < 0) {
            throw new CustomException("Ошибка сервера.");
        }
        if (!users.containsKey(user.getId())) {
            log.info("Ошибка обновления данных пользователя: " + user.getName());
            return ResponseEntity.badRequest().body(user);
        }
        users.put(user.getId(), user);
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ArrayList<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}