package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Long, Film> films = new HashMap<>();

    public long createId() {
        long id = 0;
        for (Long i : films.keySet()) {
            if (id < i) {
                id = i;
            }
        }
        return id + 1;
    }

    @Override
    public void createFilm(Film film) {
        film.setId(createId());
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
        films.put(film.getId(), film);
    }

    @Override
    public ArrayList<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmsById(long id) {
        return films.get(id);
    }
}