package ru.rest_demo.exercise_2315.configs;

import org.springframework.stereotype.Component;
import ru.rest_demo.exercise_2315.models.Role;
import ru.rest_demo.exercise_2315.models.User;
import ru.rest_demo.exercise_2315.services.RoleService;
import ru.rest_demo.exercise_2315.services.UserService;
import java.util.Set;


@Component
public class DatabaseInitializer {
    private final UserService userService;
    private final RoleService roleService;

    public DatabaseInitializer(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    //@PostConstruct
    public void AddFirstUsers() {
        Role roleAdmin = new Role("ROLE_ADMIN");
        Role roleUser = new Role("ROLE_USER");
        roleService.saveRole(roleAdmin);
        roleService.saveRole(roleUser);

        User admin = new User("admin1", "adm", 10, "a1@mail.ru", "111",
                Set.of(roleAdmin, roleUser));
        userService.saveUser(admin);

        User user = new User("user2", "usr", 20, "u2@mail.ru", "222",
                Set.of(roleUser));
        userService.saveUser(user);

    }

}
