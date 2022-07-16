package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final FilmService filmService;
    private final UserService userService;

    @Autowired
    public FilmController(FilmService filmService, UserService userService) {
        this.filmService = filmService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Film> saveFilm(@Valid @RequestBody Film film) {
        return ResponseEntity.ok().body(filmService.saveFilm(film));
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@Valid @RequestBody Film film) {

        return ResponseEntity.ok().body(filmService.updateFilm(film));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Film> getFilmById(@PathVariable long id) {
        return ResponseEntity.ok().body(filmService.getFilmById(id));
    }

    @GetMapping
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.ok().body(filmService.getAllFilms());
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Film> deleteFilmById(@PathVariable long id) {
        filmService.deleteFilmById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/popular")
    public ResponseEntity<List<Film>> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        return ResponseEntity.ok().body(filmService.getPopularFilmsList(count));
    }

    @PutMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Long> addFilmLike(@PathVariable long id, @PathVariable long userId) {
        filmService.getFilmById(id);
        userService.getUserById(userId);
        filmService.addLike(id, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{id}/like/{userId}")
    public ResponseEntity<Boolean> deleteLike(@PathVariable long id, @PathVariable long userId) {
        filmService.getFilmById(id);
        userService.getUserById(userId);
        return ResponseEntity.ok().body(filmService.deleteLike(id, userId));
    }

}