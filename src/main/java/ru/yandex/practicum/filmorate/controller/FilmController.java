package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@Valid @RequestBody Film film) {
        if (filmStorage.getFilmsById(film.getId()) != null) {
            log.info("Ошибка добавления данных: " + film.getName());
            return ResponseEntity.badRequest().body(film);
        }
        return ResponseEntity.ok().body(filmService.createFilm(film));
    }


    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {
        if (filmStorage.getFilmsById(film.getId()) == null) {
            log.info("Ошибка обновления данных Film: " + film.getName());
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(filmService.updateFilm(film));
    }


    @GetMapping
    public ResponseEntity<ArrayList<Film>> getAllFilms() {
        return ResponseEntity.ok().body(filmService.getAllFilms());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable long id) {
        if (filmStorage.getFilmsById(id) == null) {
            log.info("Ошибка параметра запроса: " + id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(filmService.getFilmById(id));
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Film> addNewLikeFilm(@PathVariable long id, @PathVariable long userId) {
        if (filmStorage.getFilmsById(id) == null || filmStorage.getFilmsById(userId) == null) {
            log.info("Ошибка параметра запроса: " + id + " " + userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(filmService.addNewLikeFilm(id, userId));
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Film> deleteLike(@PathVariable long id, @PathVariable long userId) {
        if (filmStorage.getFilmsById(id) == null || filmStorage.getFilmsById(userId) == null) {
            log.info("Ошибка параметра запроса: " + id + " " + userId);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(filmService.deleteLike(id, userId));
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") long count) {
        if (count < 0) {
            log.info("Ошибка переменной запроса: " + count);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(filmService.getPopularFilmsList(count));
    }
}