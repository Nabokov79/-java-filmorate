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
        return jdbcTemplate.queryForObject("select GENRE_ID, NAME_GENRE from GENRE  where GENRE_ID = ?",
                this::makeGenre, id);
    }

    @Override
    public List<Genre> getAllGenre() {
        return jdbcTemplate.query("select GENRE_ID, NAME_GENRE from GENRE",
                this::makeGenre);
    }

    @Override
    public void saveGenres(int genreId, long id) {
        String sqlQuery = "insert into FILMS_GENRE (GENRE_ID, FILM_ID) values (?,?)";
        jdbcTemplate.update(sqlQuery, genreId, id);
    }

    @Override
    public void deleteGenres(long filmId) {
        String sqlQuery = "delete from FILMS_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sqlQuery, filmId);
    }

    @Override
    public List<Genre> getFilmGenres(long id) {
        return jdbcTemplate.query("select g.GENRE_ID, g.NAME_GENRE " +
                                      "from FILMS_GENRE as fg " +
                                      "JOIN GENRE as g on fg.GENRE_ID = g.GENRE_ID  " +
                                      "where fg.FILM_ID = " + id +
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
