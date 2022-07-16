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

    private String name;
    @NotBlank
    @Pattern(regexp = "\\S*$")
    private String login;
    @Email
    private String email;
    @Before()
    @NotNull
    private LocalDate birthday;

    public User(long id, String email, String name, String login, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if (name == null || name.equals("")) {
            this.name = login;
        } else {
            this.name = name;
        }
        this.birthday = birthday;
    }
}
