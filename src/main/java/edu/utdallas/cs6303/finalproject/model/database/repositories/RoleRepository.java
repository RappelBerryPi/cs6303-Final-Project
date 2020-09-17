package edu.utdallas.cs6303.finalproject.model.database.repositories;

import edu.utdallas.cs6303.finalproject.model.database.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findByName(String name);

    @Override
    void delete(Role role);

}