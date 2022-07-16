package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import java.util.List;

@Component
public interface GenreStorage {

    Genre getGenreByID(int id);
    List<Genre> getAllGenre();
    void saveGenres(int genreId, long id);

    List<Genre> getFilmGenres(long id);

    void deleteGenres(long filmId);
}
