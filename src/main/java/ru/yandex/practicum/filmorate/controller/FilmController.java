package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.exeption.CustomException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Long,Film> films = new HashMap<>();

    public long createId() {
        long id = 0;
        for(Long i : films.keySet()) {
            if (id < i) {
                id = i;
            }
        }
        return id;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        film.setId(createId() + 1);
        if (films.containsKey(film.getId())) {
            log.info("Ошибка добавления данных: " + film.getName());
            return ResponseEntity.badRequest().body(film);
        }
        films.put(film.getId(), film);
        return ResponseEntity.ok().body(film);
    }

   @PutMapping
   public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (film.getId() < 0) {
            throw new CustomException("Ошибка сервера.");
        }
        if (!films.containsKey(film.getId())) {
            log.info("Ошибка обновления данных Film: " + film.getName());
            return ResponseEntity.badRequest().body(film);
        }
        films.put(film.getId(), film);
        return ResponseEntity.ok().body(film);
    }

    @GetMapping
    public ArrayList<Film> findAllFilms() {
        return new ArrayList<>(films.values());
    }
}