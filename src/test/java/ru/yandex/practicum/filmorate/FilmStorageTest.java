package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:schema.sql")
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
public class FilmStorageTest {

    @Autowired
    ObjectMapper mapper;

    @Autowired
    MockMvc mockMvc;
    private Film film;
    private Film film_2;
    private Film film_3;

    @BeforeEach
    public void addGenres() {
        List<Genre> genre = new ArrayList<>();
        genre.add(new Genre(1, " "));
        film = new Film(1,"Film", "Film _1", LocalDate.of(2022,1,1),
                                                                100, new MPA(1, "G"), genre);
        film_2 = new Film(1,"Film", "Film _2", LocalDate.of(2022,1,1),
                                                                100, new MPA(1, "G"), genre);
        film_3 = new Film(10,"Film", "Film _2", LocalDate.of(2022,1,1),
                                                                100, new MPA(1, "G"), genre);
    }

    @Test
    void saveTest() throws Exception {
        String body = mapper.writeValueAsString(film);
        MvcResult response = mockMvc.perform(post("/films")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(body))
                                    .andExpect(status().isOk())
                                    .andReturn();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
    }



    @Test
    void test1_updateFilmTest() throws Exception {
        String body = mapper.writeValueAsString(film);
        String body_1 = mapper.writeValueAsString(film_2);
        this.mockMvc.perform(post("/films")
                    .content(body)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        this.mockMvc.perform(put("/films")
                    .content(body_1)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
    }

    @Test
    void test2_updateFilmTest() throws Exception {
        String body = mapper.writeValueAsString(film_3);
        MvcResult response = mockMvc.perform(put("/films")
                                    .content(body)
                                    .contentType(MediaType.APPLICATION_JSON))
                                    .andReturn();
        int statusCod = response.getResponse().getStatus();
        assertEquals(404, statusCod, "Код ответа " + statusCod);
    }



    @Test
    void test1_getFilmById() throws Exception {
        String body = mapper.writeValueAsString(film);
        this.mockMvc.perform(post("/films")
                     .content(body)
                     .contentType(MediaType.APPLICATION_JSON))
                     .andExpect(status().isOk());
        this.mockMvc.perform(get("/films/1")
                     .contentType(MediaType.APPLICATION_JSON))
                     .andExpect(status().isOk());
    }

    @Test
    void test2_getFilmById() throws Exception {
        this.mockMvc.perform(get("/films/10")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
    }

    @Test
    void getALLFilmsTest() throws Exception {
        String body = mapper.writeValueAsString(film);
        String body_1 = mapper.writeValueAsString(film_2);
        this.mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body))
                    .andExpect(status().isOk());
        this.mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(body_1))
                    .andExpect(status().isOk());
        MvcResult response = mockMvc.perform(get("/films"))
                                    .andExpect(status().isOk())
                                    .andReturn();
        String listFilms = response.getResponse().getContentAsString();
        int statusCod = response.getResponse().getStatus();
        assertEquals(200, statusCod, "Код ответа " + statusCod);
        assertNotNull(listFilms, "Данные не вернулись");
    }
}
