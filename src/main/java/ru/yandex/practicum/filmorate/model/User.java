package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.Before;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {

    private long id;
    @Email
    private String email;
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    private String name;
    @Before()
    @NotNull
    private LocalDate birthday;
    private Set<Long> friends = new HashSet<>();

    public User(String email, String login, String name, LocalDate birthday) {
        this.email = email;
        this.login = login;
        if(name == null || name.equals("")) {
            this.name = login;
        } else { this.name = name;}
        this.birthday = birthday;
    }

    public void addFriend(long friendId) {
        friends.add(friendId);
    }
    public void deleteFriend(long userId) {
        friends.remove(userId);
    }
}