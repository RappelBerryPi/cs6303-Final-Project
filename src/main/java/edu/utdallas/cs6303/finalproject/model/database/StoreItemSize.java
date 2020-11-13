package edu.utdallas.cs6303.finalproject.model.database;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;

@Entity
public class StoreItemSize {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private StoreItem storeItem;

    @Enumerated(EnumType.STRING)
    private StoreItemSizeEnum size;

    @Column(precision = 16, scale = 2)
    private BigDecimal cost;

    public Long getId() {
        return id;
    }

    public StoreItem getStoreItem() {
        return storeItem;
    }

    public void setStoreItem(StoreItem storeItem) {
        this.storeItem = storeItem;
    }

    public StoreItemSizeEnum getSize() {
        return size;
    }

    public void setSize(StoreItemSizeEnum size) {
        this.size = size;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
}
