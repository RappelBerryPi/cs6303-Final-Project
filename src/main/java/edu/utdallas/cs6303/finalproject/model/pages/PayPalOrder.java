package edu.utdallas.cs6303.finalproject.model.pages;

import java.util.List;

import com.paypal.orders.LinkDescription;
import com.paypal.orders.Order;
import com.paypal.orders.Payer;
import com.paypal.orders.PurchaseUnit;

public class PayPalOrder extends Order {

    public PayPalOrder(Order order) {
        this.createTime(order.createTime());
        this.expirationTime(order.expirationTime());
        this.id(order.id());
        this.links(order.links());
        this.payer(order.payer());
        this.purchaseUnits(order.purchaseUnits());
        this.status(order.status());
        this.updateTime(order.updateTime());
        this.checkoutPaymentIntent(order.checkoutPaymentIntent());
    }

    public String getCreateTime() {
        return super.createTime();
    }

    public String getExpirationTime() {
        return super.expirationTime();
    }

    public String getId() {
        return super.id();
    }

    public List<LinkDescription> getLinks() {
        return super.links();
    }

    public Payer getPayer() {
        return super.payer();
    }

    public List<PurchaseUnit> getPurchaseUnits() {
        return super.purchaseUnits();
    }

    public String getStatus() {
        return super.status();
    }

    public String getUpdateTime() {
        return super.updateTime();
    }

    public String getCheckoutPaymentIntent() {
        return super.checkoutPaymentIntent();
    }

}
