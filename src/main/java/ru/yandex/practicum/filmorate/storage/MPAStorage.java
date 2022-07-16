package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import java.util.List;
@Component
public interface MPAStorage {

    MPA getMPAByID(int id);
    List<MPA> getAllMPA();
}
