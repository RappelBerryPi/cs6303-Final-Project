package edu.utdallas.cs6303.finalproject.services.order;

import java.util.Collection;

import edu.utdallas.cs6303.finalproject.model.database.Order;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.User;

public interface OrderServiceInterface {
    Order buildOrderFromUserCart(User user);

    void addItemToUserCart(User user, StoreItem item);

    void removeItemFromUserCart(User user, StoreItem item);

    void clearUserCart(User user);

    Collection<Order> getUserOrders(User user);
}