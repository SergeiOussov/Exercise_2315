package ru.rest_demo.exercise_2315.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.rest_demo.exercise_2315.models.User;
import java.util.List;

public interface UserService extends UserDetailsService {
    User findUserById(Long userId);

    User findByEmail(String email);

    List<User> allUsers();

    void saveUser(User user);

    void updateUser(User user);

    void deleteUserById(Long userId);
}
