package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.OrderTicket;
import edu.utdallas.cs6303.finalproject.model.database.User;

@Repository
public interface OrderTicketRepository extends JpaRepository<OrderTicket, Long> {
    List<OrderTicket> findByUser(User user);
    
}