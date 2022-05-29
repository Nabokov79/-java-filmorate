package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exeption.ControllerException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@Validated
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer,Film> films = new HashMap<>();

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody(required = false) Film film) {
        try  {
            if (films.containsKey(film.getId())) {
                log.info("Ошибка добавления данных: " + film.getName());
                return ResponseEntity.ok().body(film);
            }
            films.put(film.getId(), film);
        } catch (NullPointerException e) {
            log.error("Отсутствуют данные в запросе: " + e.getMessage());
            throw new ControllerException("Отсутствуют данные в запросе: " + e.getMessage());
        }
        return ResponseEntity.ok().body(film);
    }

    @PutMapping
   public ResponseEntity<Film> updateFilm(@Valid @RequestBody(required = false) Film film) {
        try  {
            if (!films.containsKey(film.getId())) {
                log.info("Ошибка обновления данных Film: " + film.getName());
                return ResponseEntity.status(500).body(film);
            }
            films.put(film.getId(), film);
        } catch (NullPointerException e) {
            log.error("Отсутствуют данные в запросе: " + e.getMessage());
            throw new ControllerException("Отсутствуют данные в запросе: " + e.getMessage());
        }
        return ResponseEntity.ok().body(film);
    }

    @GetMapping
    public ArrayList<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }
}
