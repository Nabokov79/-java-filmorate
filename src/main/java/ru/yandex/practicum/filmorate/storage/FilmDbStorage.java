package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MPAStorage mpaStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate, MPAStorage mpaStorage, GenreStorage genreStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaStorage = mpaStorage;
        this.genreStorage = genreStorage;
    }

    @Override
    public void save(Film film) {
        String sqlQuery = "INSERT INTO FILMS (MPA_ID, NAME_FILM, RELEASE_DATE, DESCRIPTION, DURATION) " +
                          "VALUES (?,?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setInt(1, film.getMpa().getId());
            stmt.setString(2, film.getName());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setString(4, film.getDescription());
            stmt.setInt(5, film.getDuration());
            return stmt;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.saveGenres(genre.getId(), film.getId());
            }
        }
    }

    @Override
    public void updateFilm(Film film) {
        String sqlQuery = "UPDATE FILMS " +
                          "SET NAME_FILM = ?, RELEASE_DATE = ?, DESCRIPTION = ?, DURATION = ?, MPA_ID = ? " +
                          "WHERE FILM_ID = ?";
        jdbcTemplate.update(sqlQuery
                            , film.getName()
                            , Date.valueOf(film.getReleaseDate())
                            , film.getDescription()
                            , film.getDuration()
                            , film.getMpa().getId()
                            , film.getId());
        genreStorage.deleteGenres(film.getId());
        if (film.getGenres() != null) {
            for (Genre genre : film.getGenres()) {
                genreStorage.saveGenres(genre.getId(), film.getId());
            }
        }
    }

    @Override
    public Optional<Film> getFilmsById(long id) {
        String sqlQuery = "SELECT FILM_ID, NAME_FILM, RELEASE_DATE, DESCRIPTION, DURATION, MPA_ID " +
                          "FROM FILMS " +
                          "WHERE FILM_ID = ?";
        List<Film> result = jdbcTemplate.query(sqlQuery,
                this::makeFilm, id);
        return result.size() == 0 ?
                Optional.empty():
                Optional.of(result.get(0));
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT FILM_ID, MPA_ID, NAME_FILM, RELEASE_DATE, DESCRIPTION, DURATION " +
                          "FROM FILMS";
        return jdbcTemplate.query(sqlQuery,
                this::makeFilm);
    }

    @Override
    public void deleteFilmById(long id) {
        String sqlQuery = "DELETE FROM FILMS WHERE FILM_ID = ?";
       jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public List<Film> getPopularFilmsList(int count) {
        String sqlQuery = "SELECT FILMS.FILM_ID, FILMS.MPA_ID, FILMS.NAME_FILM, FILMS.RELEASE_DATE,FILMS.DESCRIPTION, " +
                                 "FILMS.DURATION, COUNT(LIKES.USER_ID) " +
                          "FROM FILMS " +
                          "LEFT JOIN LIKES ON FILMS.FILM_ID = LIKES.FILM_ID " +
                          "GROUP BY FILMS.FILM_ID, LIKES.USER_ID " +
                          "ORDER BY LIKES.USER_ID DESC  " +
                          "LIMIT " + count ;
        return jdbcTemplate.query(sqlQuery,
                this::makeFilm);
    }

    @Override
    public void addLike(long id, long userId) {
        String sqlQuery = "INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"LIKE_ID"});
            stmt.setLong(1, id);
            stmt.setLong(2, userId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public boolean deleteLike(long id, long userId) {
        String sqlQuery = "DELETE FROM LIKES WHERE FILM_ID = ? AND USER_ID = ?" ;
        return jdbcTemplate.update(sqlQuery, id, userId) > 0;
    }

    private Film makeFilm(ResultSet rs, int rowNum) throws SQLException {
        return new Film(
                rs.getLong("FILM_ID"),
                rs.getString("NAME_FILM"),
                rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(),
                rs.getInt("DURATION"),
                mpaStorage.getMPAByID(rs.getInt("MPA_ID")),
                genreStorage.getFilmGenres(rs.getLong("FILM_ID"))
        );
    }
}