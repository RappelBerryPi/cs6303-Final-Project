package edu.utdallas.cs6303.finalproject.model.database.repositories;

import edu.utdallas.cs6303.finalproject.model.database.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findById(Integer id);
    User findByEmail(String email);

    @Override
    void delete(User user);
}