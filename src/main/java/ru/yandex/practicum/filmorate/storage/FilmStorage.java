package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;

public interface FilmStorage {

    void createFilm(Film film);
    void updateFilm(Film film);
    ArrayList<Film> getAllFilms();
    Film getFilmsById(long id);
}
