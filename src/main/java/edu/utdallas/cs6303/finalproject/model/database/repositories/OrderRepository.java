package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.Order;
import edu.utdallas.cs6303.finalproject.model.database.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    
}