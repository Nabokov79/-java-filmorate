package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import java.util.*;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final Set<Film> sortedFilmList = new TreeSet<>((o1, o2) ->
         Integer.compare(o2.getLike().size(), o1.getLike().size())
    );

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public Film createFilm(Film film) {
        filmStorage.createFilm(film);
        return film;
    }

    public Film updateFilm(Film film) {
        filmStorage.updateFilm(film);
        return film;
    }

    public ArrayList<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(long id) {
        return filmStorage.getFilmsById(id);
    }

    public Film addNewLikeFilm(long id, long userId) {
        Film film = filmStorage.getFilmsById(id);
        film.addLike(userId);
        filmStorage.updateFilm(film);
        return film;
    }

    public Film deleteLike(long id, long userId) {
        filmStorage.getFilmsById(id).deleteLike(userId);
        return filmStorage.getFilmsById(id);
    }

    public List<Film> getPopularFilmsList(long count) {
        sortedFilmList.addAll(filmStorage.getAllFilms());
        List<Film> listFilm = new ArrayList<>();
        for (Film film : sortedFilmList) {
            if (listFilm.size() < count) {
                listFilm.add(film);
            }
        }
        return listFilm;
    }
}