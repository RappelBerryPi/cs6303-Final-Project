package edu.utdallas.cs6303.finalproject.model.database.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.utdallas.cs6303.finalproject.model.database.StoreItemSize;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;

@Repository
public interface StoreItemSizeRepository extends JpaRepository<StoreItemSize, Long> {

    StoreItemSize findFirstByStoreItemIdEqualsAndSizeIs(long storeItemId, StoreItemSizeEnum size);

}