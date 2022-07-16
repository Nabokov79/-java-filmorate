package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.yandex.practicum.filmorate.model.User;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class UserStorageTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;
    private User user;
    private User user_2;
    private User user_3;

    @BeforeEach
    public void addUsers() {
        user = new User(1,"mail@mail.ru", "login", "name"
                , LocalDate.of(1967, 3, 25));
        user_2 = new User(1,"mail@mail.ru", "User", "name"
                , LocalDate.of(1969, 5, 25));
        user_3 = new User(10,"mail@mail.ru", "User", "name"
                , LocalDate.of(1969, 5, 25));

    }

    @Test
    void saveUserTest() throws Exception {
        String body = mapper.writeValueAsString(user);
        MvcResult response = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andReturn();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
    }

    @Test
    void test1_updateUserTest() throws Exception {
        String body = mapper.writeValueAsString(user);
        String body_1 = mapper.writeValueAsString(user_2);
        this.mockMvc.perform(post("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(put("/users")
                        .content(body_1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_updateUserTest() throws Exception {
        String body = mapper.writeValueAsString(user_3);
        MvcResult response = mockMvc.perform(put("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        int statusCod = response.getResponse().getStatus();
        assertEquals(404, statusCod, "Код ответа " + statusCod);
    }
    @Test
    void test1_getUserByIdTest() throws Exception {
        String body = mapper.writeValueAsString(user);
        this.mockMvc.perform(post("/users")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        this.mockMvc.perform(get("/users/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void test2_getUserByIdTest() throws Exception {
        this.mockMvc.perform(get("/users/10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
