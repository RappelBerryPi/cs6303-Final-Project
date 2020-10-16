package edu.utdallas.cs6303.finalproject.services.store;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.repositories.StoreItemRepository;

@Service
public class StoreService implements StoreServiceInterface {

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Override
    public List<StoreItem> findAll() {
        return storeItemRepository.findAll();
    }

    @Override
    public Optional<StoreItem> findById(long id) {
        return storeItemRepository.findById(id);
    }
    
}
