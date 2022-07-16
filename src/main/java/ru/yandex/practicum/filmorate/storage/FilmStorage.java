package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.List;

@Component
public interface FilmStorage {

    void save(Film film);
    void updateFilm(Film film);
    Film getFilmsById(long id);
    List<Film> getAllFilms();
    void deleteFilmById(long id);
    List<Film> getPopularFilmsList(int count);
    void addLike(long id, long userId);
    boolean deleteLike(long id, long userId);
}
