package edu.utdallas.cs6303.finalproject.model.database.repositories;

import edu.utdallas.cs6303.finalproject.model.database.Privilege;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends JpaRepository<Privilege, Long> {
    Privilege findByName(String name);

    @Override
    void delete(Privilege privilege);
}