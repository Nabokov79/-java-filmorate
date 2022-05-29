package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import ru.yandex.practicum.filmorate.exeption.ControllerException;
import ru.yandex.practicum.filmorate.model.User;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Validated
@Slf4j
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        try  {
            if (users.containsKey(user.getId())) {
                log.info("Ошибка добавления данных пользователя: " + user.getName());
                return ResponseEntity.ok().body(user);
            }
            users.put(user.getId(), user);
        } catch (NullPointerException e) {
            log.error("Отсутствуют данные в запросе: " + e.getMessage());
            throw new ControllerException("Отсутствуют данные в запросе: " + e.getMessage());
        }
        return ResponseEntity.ok().body(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@Valid @RequestBody User user) {
        try  {
            if (!users.containsKey(user.getId())) {
                log.info("Ошибка обновления данных пользователя: " + user.getName());
                return ResponseEntity.status(500).body(user);
            }
            users.put(user.getId(), user);
        } catch (NullPointerException e) {
            log.error("Отсутствуют данные в запросе: " + e.getMessage());
            throw new ControllerException("Отсутствуют данные в запросе: " + e.getMessage());
        }
        return ResponseEntity.ok().body(user);
    }

    @GetMapping
    public ArrayList<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }
}
