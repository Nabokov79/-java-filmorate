package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;
import java.util.List;

@Controller
@RequestMapping("/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Genre> getGenreByID(@PathVariable int id) {
        if ( id < 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(genreService.getGenreByID(id));
    }

    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenre() {
        return ResponseEntity.ok().body(genreService.getAllGenre());
    }
}
