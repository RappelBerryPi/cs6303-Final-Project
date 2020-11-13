package edu.utdallas.cs6303.finalproject.model.database;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class OrderTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems;

    @ManyToOne
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "FK_StoreItem_FeaturedPhoto"))
    private User user;

    @Column(precision = 16, scale = 2)
    private BigDecimal subTotal;

    @Column(precision = 16, scale = 2)
    private BigDecimal tax;

    private String payPalOrderId;

    public long getId() {
        return id;
    }

    public String getPayPalOrderId() {
        return payPalOrderId;
    }

    public void setPayPalOrderId(String payPalOrderId) {
        this.payPalOrderId = payPalOrderId;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return this.getSubTotal().add(this.getTax());
    }

    public boolean isOrderPlaced() {
        return orderItems.stream().anyMatch(o -> o.getDateOrdered() != null);
    }

    public boolean isOrderPaid() {
        return orderItems.stream().allMatch(o -> o.getDatePaid() != null);
    }

    public boolean orderSentToCustomer() {
        return orderItems.stream().allMatch(o -> o.getDateSentToCustomer() != null);
    }

    public boolean orderDeliveredToCustomer() {
        return orderItems.stream().allMatch(o -> o.getDateDelivered() != null);
    }

}
