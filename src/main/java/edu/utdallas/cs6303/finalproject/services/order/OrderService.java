package edu.utdallas.cs6303.finalproject.services.order;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.utdallas.cs6303.finalproject.model.database.Cart;
import edu.utdallas.cs6303.finalproject.model.database.Order;
import edu.utdallas.cs6303.finalproject.model.database.OrderItem;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.CartRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.OrderItemRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.OrderRepository;

@Service
public class OrderService implements OrderServiceInterface {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public Order buildOrderFromUserCart(User user) {
        if (user.getCart().isEmpty()) {
            throw new CartEmptyException("Unable to create order, Cart is empty");
        }

        Order order = new Order();
        order.setUser(user);
        List<OrderItem> orderItems = user.getCart().getCartItems().stream().map(this::convertStoreItemToOrderItem).collect(Collectors.toList());
        BigDecimal      subTotal   = orderItems.stream().map(OrderItem::getCostAtPurchaseTime).reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setOrderItems(orderItems);
        order.setSubTotal(subTotal);
        order.setTax(subTotal.multiply(new BigDecimal(SALES_TAX)));
        order = orderRepository.save(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }
        orderItemRepository.saveAll(orderItems);
        return order;
    }

    public static final String SALES_TAX = "0.0825";

    private OrderItem convertStoreItemToOrderItem(StoreItem storeItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setStoreItem(storeItem);
        orderItem.setCostAtPurchaseTime(storeItem.getCost());
        return orderItem;
    }

    @Override
    public void addItemToUserCart(User user, StoreItem item) {
        Cart cart = user.getCart();
        cart.getCartItems().add(item);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromUserCart(User user, StoreItem item) {
        Cart cart = user.getCart();
        cart.getCartItems().remove(item);
        cartRepository.save(cart);
    }

    @Override
    public void clearUserCart(User user) {
        Cart cart = user.getCart();
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public Collection<Order> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

}
