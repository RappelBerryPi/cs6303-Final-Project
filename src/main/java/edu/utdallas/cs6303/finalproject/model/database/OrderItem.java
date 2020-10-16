package edu.utdallas.cs6303.finalproject.model.database;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

@Entity
public class OrderItem {

    @Id
    private long id;
    
    @OneToOne
    private StoreItem storeItem;

    @ManyToOne
    private Order order;

    @Column(precision = 16, scale = 2)
    private BigDecimal costAtPurchaseTime;

    private ZonedDateTime dateOrdered;
    private ZonedDateTime datePaid;
    private ZonedDateTime dateSentToCustomer;
    private ZonedDateTime dateDelivered;

    public OrderItem() {
        this.setDateDelivered(null);
        this.setDateOrdered(null);
        this.setDatePaid(null);
        this.setDateSentToCustomer(null);
    }

    public long getId() {
        return id;
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }

    public void setStoreItem(StoreItem storeItem) {
        this.storeItem = storeItem;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public BigDecimal getCostAtPurchaseTime() {
        return costAtPurchaseTime;
    }

    public void setCostAtPurchaseTime(BigDecimal costAtPurchaseTime) {
        this.costAtPurchaseTime = costAtPurchaseTime;
    }

    public ZonedDateTime getDateOrdered() {
        return dateOrdered;
    }

    public void setDateOrdered(ZonedDateTime dateOrdered) {
        this.dateOrdered = dateOrdered;
    }

    public ZonedDateTime getDatePaid() {
        return datePaid;
    }

    public void setDatePaid(ZonedDateTime datePaid) {
        this.datePaid = datePaid;
    }

    public ZonedDateTime getDateSentToCustomer() {
        return dateSentToCustomer;
    }

    public void setDateSentToCustomer(ZonedDateTime dateSentToCustomer) {
        this.dateSentToCustomer = dateSentToCustomer;
    }

    public ZonedDateTime getDateDelivered() {
        return dateDelivered;
    }

    public void setDateDelivered(ZonedDateTime dateDelivered) {
        this.dateDelivered = dateDelivered;
    }

}
