package ru.rest_demo.exercise_2315.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.rest_demo.exercise_2315.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
