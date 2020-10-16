package edu.utdallas.cs6303.finalproject.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {

}
