package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();

    public long createId() {
        long id = 0;
        for(Long i : users.keySet()) {
            if (id < i) {
                id = i;
            }
        }
        return id + 1;
    }

    @Override
    public void createUser( User user) {
        user.setId(createId());
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(long id) {
        return users.get(id);
    }

    @Override
    public void deleteUserById(long id) {
        users.remove(id);
    }
}