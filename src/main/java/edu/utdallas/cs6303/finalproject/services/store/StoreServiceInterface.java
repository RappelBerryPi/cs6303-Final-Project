package edu.utdallas.cs6303.finalproject.services.store;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import edu.utdallas.cs6303.finalproject.model.database.Cart;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;
import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;
import edu.utdallas.cs6303.finalproject.model.pages.CartSummary;
import edu.utdallas.cs6303.finalproject.model.pages.ChangeInformation;
import edu.utdallas.cs6303.finalproject.model.validation.store.GroupItemForm;

public interface StoreServiceInterface {

    List<StoreItem> findAll();

    Optional<StoreItem> findById(long id); 

    List<StoreItem> findByCategoryIn(Collection<StoreItemCategoryEnum> categories);

    StoreItem createStoreItemFromForm(GroupItemForm itemForm);

    int addItemToCart(long id, StoreItemSizeEnum size);

    Cart getUserCart();

    Map<StoreItem,List<CartSummary>> createUserCartSummary();

    ChangeInformation deleteItemFromCart(long storeItemSizeId, long newQuantity);

    BigDecimal getSubtotalOfCart();

}
