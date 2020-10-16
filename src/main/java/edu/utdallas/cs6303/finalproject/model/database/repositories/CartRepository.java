package edu.utdallas.cs6303.finalproject.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.utdallas.cs6303.finalproject.model.database.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {

}
