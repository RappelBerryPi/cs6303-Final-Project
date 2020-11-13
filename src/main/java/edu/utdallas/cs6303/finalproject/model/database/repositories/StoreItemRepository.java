package edu.utdallas.cs6303.finalproject.model.database.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;

@Repository
public interface StoreItemRepository extends JpaRepository<StoreItem, Long> {

    List<StoreItem> findByCategoryIn(Collection<StoreItemCategoryEnum> categories);
    Optional<StoreItem> findFirstByName(String name);

}
