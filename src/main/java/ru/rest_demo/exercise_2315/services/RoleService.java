package ru.rest_demo.exercise_2315.services;

import ru.rest_demo.exercise_2315.models.Role;
import java.util.List;

public interface RoleService {
    List<Role> allRoles();

    void saveRole(Role role);
}
