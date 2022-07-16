package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.CustomException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film saveFilm(Film film) {
        filmStorage.save(film);
        return getFilmById(film.getId());
    }

    public Film updateFilm(Film film) {
        getFilmById(film.getId());
        filmStorage.updateFilm(film);
        return getFilmById(film.getId());
    }

    public Film getFilmById(long id) {
        try {
            return filmStorage.getFilmsById(id);
        } catch (DataAccessException dax) {
            log.info("Film id = " + id + " not found.");
            throw new CustomException("Film id = " + id + " not found.");
        }
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public void deleteFilmById(long id) {
        getFilmById(id);
        filmStorage.deleteFilmById(id);
    }

    public List<Film> getPopularFilmsList(int count) {
        return filmStorage.getPopularFilmsList(count);
    }

    public void addLike(long id, long userId) {
        filmStorage.addLike(id, userId);
    }

    public boolean deleteLike(long id, long userId) {
        return filmStorage.deleteLike(id, userId);
    }
}