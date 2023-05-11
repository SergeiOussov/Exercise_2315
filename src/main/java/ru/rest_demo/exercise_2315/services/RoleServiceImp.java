package ru.rest_demo.exercise_2315.services;

import org.springframework.stereotype.Service;
import ru.rest_demo.exercise_2315.models.Role;
import ru.rest_demo.exercise_2315.repositories.RoleRepository;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class RoleServiceImp implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImp(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> allRoles() {
        return roleRepository.findAll();
    }

    @Override
    @Transactional
    public void saveRole(Role role) { roleRepository.save(role); }
}
