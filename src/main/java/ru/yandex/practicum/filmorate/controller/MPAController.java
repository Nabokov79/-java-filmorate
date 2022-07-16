package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.MPAService;

import java.util.List;

@Controller
@RequestMapping("/mpa")
public class MPAController {
    private final MPAService mpaService;

    @Autowired
    public MPAController(MPAService mpaService) {
        this.mpaService = mpaService;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<MPA> getMPAByID(@PathVariable int id) {
        if ( id < 0) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(mpaService.getMPAByID(id));
    }

    @GetMapping
    public ResponseEntity<List<MPA>> getAllMPA() {
        return ResponseEntity.ok().body(mpaService.getAllMPA());
    }
}