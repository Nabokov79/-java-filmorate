package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class GenreDbStorage implements GenreStorage{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre getGenreByID(int id) {
        return jdbcTemplate.queryForObject("SELECT GENRE_ID, NAME_GENRE FROM GENRE WHERE GENRE_ID = ?",
                this::makeGenre, id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("SELECT GENRE_ID, NAME_GENRE FROM GENRE",
                this::makeGenre);
    }

    @Override
    public void saveGenres(int genreId, long id) {
        String sqlQuery = "INSERT INTO FILMS_GENRE (GENRE_ID, FILM_ID) VALUES (?,?)";
        jdbcTemplate.update(sqlQuery, genreId, id);
    }

    @Override
    public void deleteGenres(long filmId) {
        String sqlQuery = "DELETE FROM FILMS_GENRE WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Genre> getFilmGenres(long id) {
        return jdbcTemplate.query("SELECT g.GENRE_ID, g.NAME_GENRE " +
                                      "FROM FILMS_GENRE AS fg " +
                                      "JOIN GENRE AS g ON fg.GENRE_ID = g.GENRE_ID  " +
                                      "WHERE fg.FILM_ID = " + id +
                                      " GROUP BY fg.GENRE_ID",
                this::makeGenre);
    }


    private Genre makeGenre (ResultSet rs, int rowNum) throws SQLException {
        return new Genre (
                rs.getInt("GENRE_ID"),
                rs.getString("NAME_GENRE")
        );
    }
}
