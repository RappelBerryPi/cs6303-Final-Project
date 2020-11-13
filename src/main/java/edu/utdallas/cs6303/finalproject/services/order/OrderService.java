package edu.utdallas.cs6303.finalproject.services.order;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import edu.utdallas.cs6303.finalproject.model.database.Cart;
import edu.utdallas.cs6303.finalproject.model.database.OrderTicket;
import edu.utdallas.cs6303.finalproject.model.database.OrderItem;
import edu.utdallas.cs6303.finalproject.model.database.StoreItem;
import edu.utdallas.cs6303.finalproject.model.database.StoreItemSize;
import edu.utdallas.cs6303.finalproject.model.database.User;
import edu.utdallas.cs6303.finalproject.model.database.repositories.CartRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.OrderItemRepository;
import edu.utdallas.cs6303.finalproject.model.database.repositories.OrderTicketRepository;
import edu.utdallas.cs6303.finalproject.model.pages.CartSummary;
import edu.utdallas.cs6303.finalproject.services.store.StoreServiceInterface;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.orders.AddressPortable;
import com.paypal.orders.AmountBreakdown;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Item;
import com.paypal.orders.Money;
import com.paypal.orders.Name;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;
import com.paypal.orders.ShippingDetail;

@Service
public class OrderService implements OrderServiceInterface {

    @Autowired
    private OrderTicketRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PayPalHttpClient client;

    @Autowired
    private StoreServiceInterface storeService;

    @Override
    public OrderTicket buildOrderFromUserCart(User user, String payPalOrderId) {
        if (user.getCart().isEmpty()) {
            throw new CartEmptyException("Unable to create order, Cart is empty");
        }
        OrderTicket order = new OrderTicket();
        order.setUser(user);
        storeService.getSubtotalOfCart();
        List<OrderItem> orderItems = user.getCart().getCartItems().stream().map(this::convertStoreItemToOrderItem).collect(Collectors.toList());
        order.setOrderItems(orderItems);
        order.setSubTotal(storeService.getSubtotalOfCart());
        order.setTax(storeService.createUserCartSummary().values().stream().flatMap(l -> l.stream()).map(s -> s.getCost().multiply(new BigDecimal(OrderService.SALES_TAX)).setScale(2,RoundingMode.HALF_UP).multiply(new BigDecimal(s.getCount()))).reduce(BigDecimal.ZERO, (a, b) -> a.add(b)));
        order.setPayPalOrderId(payPalOrderId);
        order = orderRepository.save(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order);
        }
        orderItemRepository.saveAll(orderItems);
        this.clearUserCart(user);
        return order;
    }

    public static final String SALES_TAX = "0.0825";

    private OrderItem convertStoreItemToOrderItem(StoreItemSize storeItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setStoreItem(storeItem);
        orderItem.setCostAtPurchaseTime(storeItem.getCost());
        return orderItem;
    }

    @Override
    public void addItemToUserCart(User user, StoreItemSize item) {
        Cart cart = user.getCart();
        cart.getCartItems().add(item);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromUserCart(User user, StoreItemSize item) {
        Cart cart = user.getCart();
        cart.getCartItems().remove(item);
        cartRepository.save(cart);
    }

    @Override
    public void clearUserCart(User user) {
        Cart cart = user.getCart();
        var cartItems = cart.getCartItems();
        cartItems.clear();
        cart.setCartItems(cartItems);
        cartRepository.save(cart);
    }

    @Override
    public Collection<OrderTicket> getUserOrders(User user) {
        return orderRepository.findByUser(user);
    }

    @Override
    public HttpResponse<com.paypal.orders.Order> createPaypalOrder(Map<StoreItem, List<CartSummary>> cartSummary) throws IOException {
        OrdersCreateRequest request = new OrdersCreateRequest();
        request.prefer("return=representation");
        request.requestBody(getRequestBody(cartSummary));
        return client.execute(request);
    }

    private OrderRequest getRequestBody(Map<StoreItem, List<CartSummary>> cartSummary) {
        OrderRequest       orderRequest = new OrderRequest();
        orderRequest.checkoutPaymentIntent("CAPTURE");
        ApplicationContext context      = new ApplicationContext().brandName("Mom and Pop's Gourmet Popcorn Shop");
        orderRequest.applicationContext(context);

        List<Item> items = new ArrayList<>();
        for (var entry: cartSummary.entrySet()) {
            for (var value: entry.getValue()) {
                Item item = new Item()
                    .name(entry.getKey().getName() + " " + StringUtils.capitalize(value.getSize().toString().toLowerCase()))
                    .description(entry.getKey().getShortDescription())
                    .sku("sku" + entry.getKey().getId())
                    .unitAmount(new Money().currencyCode("USD").value(value.getCost().toString()))
                    .tax(new Money().currencyCode("USD").value(value.getCost().multiply(new BigDecimal(SALES_TAX)).setScale(2,RoundingMode.HALF_UP).toString()))
                    .quantity(Long.toString(value.getCount()))
                    ;
                    //.category(entry.getKey().getCategory().toString());
                items.add(item);
            }
        }
        BigDecimal subtotal = BigDecimal.ZERO;
        BigDecimal tax = BigDecimal.ZERO;
        for (var item: items) {
            tax = tax.add(new BigDecimal(item.tax().value()).multiply(new BigDecimal(item.quantity())));
            subtotal = subtotal.add(new BigDecimal(item.unitAmount().value()).multiply(new BigDecimal(item.quantity())));
        }
        BigDecimal total = tax.add(subtotal);
        PurchaseUnitRequest       purchaseUnitRequest  = new PurchaseUnitRequest()
            .referenceId("ABCD").description("Popcorn").customId("ABCD-POPCORN")
            .softDescriptor("Mom and Pop's Shop")
            .amountWithBreakdown(new AmountWithBreakdown()
                .currencyCode("USD")
                .value(total.toString())
                .amountBreakdown(new AmountBreakdown()
                    .itemTotal(new Money()
                        .currencyCode("USD").value(subtotal.toString()))
                    .taxTotal(new Money().currencyCode("USD").value(tax.toString()))
                    .shipping(new Money().currencyCode("USD").value("0.00"))))
                .items(items)
                .shippingDetail(new ShippingDetail()
                    .name(new Name().fullName("John Doe"))
                    .addressPortable(new AddressPortable()
                        .addressLine1("123 Townsend St")
                        .addressLine2("Floor 6")
                        .adminArea2("San Francisco")
                        .adminArea1("CA")
                        .postalCode("94107")
                        .countryCode("US")));

        List<PurchaseUnitRequest> purchaseUnitRequests = new ArrayList<>();
        purchaseUnitRequests.add(purchaseUnitRequest);
        orderRequest.purchaseUnits(purchaseUnitRequests);

        return orderRequest;
    }

}
