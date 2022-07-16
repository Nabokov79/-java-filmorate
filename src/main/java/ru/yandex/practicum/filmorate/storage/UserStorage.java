package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import java.util.List;

@Component public interface UserStorage {

    void saveUser(User user);
    void updateUser(User user);
    User getUserById(long id);
    List<User> getAllUsers();
    void deleteUserById(long id);
    void addFriend(long id, long friendId);
    void deleteFriends(long id, long friendId);
    List<User> getListUserFriends(long id);
    List<User> getListMutualFriends(long id, long otherId);

}
