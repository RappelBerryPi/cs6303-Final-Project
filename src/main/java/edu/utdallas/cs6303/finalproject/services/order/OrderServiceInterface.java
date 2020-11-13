package edu.utdallas.cs6303.finalproject.services.order;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.paypal.http.HttpResponse;

import edu.utdallas.cs6303.finalproject.model.database.OrderTicket;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.StoreItemSize;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.pages.CartSummary;

public interface OrderServiceInterface {
    OrderTicket buildOrderFromUserCart(User user, String payPalOrderId);

    void addItemToUserCart(User user, StoreItemSize item);

    void removeItemFromUserCart(User user, StoreItemSize item);

    void clearUserCart(User user);

    Collection<OrderTicket> getUserOrders(User user);

    HttpResponse<com.paypal.orders.Order> createPaypalOrder(Map<StoreItem, List<CartSummary>> cartSummary) throws IOException;
}