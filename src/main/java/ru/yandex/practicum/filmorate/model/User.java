package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validators.Before;
import javax.validation.constraints.*;
import java.time.LocalDate;

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

    public User(long id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        if(name == null || name.equals("")) {
            this.name = login;
        } else { this.name = name;}
        this.birthday = birthday;
    }

    public long addNewIdUser() {
        return id += 1;
    }
}