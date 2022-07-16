package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Repository
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public void saveUser(User user) {
        String sqlQuery = "insert into USERS ( EMAIL, NAME_USER, LOGIN_USER,BIRTHDAY) values (?,?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getLogin());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    @Override
    public void updateUser(User user) {
        String sqlQuery = " update USERS set  EMAIL = ?, NAME_USER = ?, LOGIN_USER = ?, BIRTHDAY = ? where USER_ID = ?";
        jdbcTemplate.update(sqlQuery
                , user.getEmail()
                , user.getName()
                , user.getLogin()
                , user.getBirthday()
                , user.getId());
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "select USER_ID, EMAIL, NAME_USER, LOGIN_USER, BIRTHDAY from USERS";
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("NAME_USER"),
                        rs.getString("LOGIN_USER"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ));
    }

    @Override
    public Optional<User> getUserById(long id) {
        String sqlQuery = "select USER_ID, EMAIL, NAME_USER, LOGIN_USER,  BIRTHDAY from USERS where USER_ID = ?";
        List<User> result = jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("NAME_USER"),
                        rs.getString("LOGIN_USER"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ), id);
        return result.size() == 0 ?
                Optional.empty():
                Optional.of(result.get(0));
    }

    @Override
    public void deleteUserById(long id) {
        String sqlQuery = "delete from USERS where USER_ID = ?";
        jdbcTemplate.update(sqlQuery, id);
    }

    @Override
    public void addFriend(long id, long friendId) {
        String sqlQuery = "insert into FRIENDS (USER_ID, FRIEND_ID) values (?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"USER_ID"});
            stmt.setLong(1, id);
            stmt.setLong(2, friendId);
            return stmt;
        }, keyHolder);
    }

    @Override
    public void deleteFriends(long id, long friendId) {
        String sqlQuery = "delete from FRIENDS where USER_ID = ? and FRIEND_ID = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getListUserFriends(long id) {
        String sqlQuery = "select us.USER_ID, us.EMAIL, us.NAME_USER, us.LOGIN_USER,  us.BIRTHDAY "
                        + "from USERS as u "
                        + "JOIN FRIENDS as f on u.USER_ID = f.USER_ID "
                        + "JOIN USERS as us on f.FRIEND_ID = us.USER_ID "
                        + "WHERE u.USER_ID = " + id;
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("NAME_USER"),
                        rs.getString("LOGIN_USER"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ));
    }

    @Override
    public List<User> getListMutualFriends(long id, long otherId) {
        String sqlQuery = "select f.FRIEND_ID, u.USER_ID, u.EMAIL, u.NAME_USER, u.LOGIN_USER, u.BIRTHDAY "
                        + "from FRIENDS as f, FRIENDS as fr "
                        + "JOIN USERS as u on u.USER_ID = f.FRIEND_ID "
                        + "where f.FRIEND_ID = fr.FRIEND_ID and f.USER_ID = " + id + " and fr.USER_ID = " + otherId;
        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new User(
                        rs.getLong("USER_ID"),
                        rs.getString("EMAIL"),
                        rs.getString("NAME_USER"),
                        rs.getString("LOGIN_USER"),
                        rs.getDate("BIRTHDAY").toLocalDate()
                ));
    }
}