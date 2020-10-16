package edu.utdallas.cs6303.finalproject.services.store;

import java.util.List;
import java.util.Optional;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;

public interface StoreServiceInterface {

    List<StoreItem> findAll();

    Optional<StoreItem> findById(long id); 

}
