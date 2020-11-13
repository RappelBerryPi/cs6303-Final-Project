package edu.utdallas.cs6303.finalproject.model.pages;

import java.math.BigDecimal;

import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemSizeEnum;

public class CartSummary {

    private BigDecimal        cost;
    private long              count;
    private StoreItemSizeEnum size;
    private String            itemName;
    private long              storeItemSizeId;

    public BigDecimal getCost() {
        return cost;
    }

    public long getStoreItemSizeId() {
        return storeItemSizeId;
    }

    public void setStoreItemSizeId(long storeItemSizeId) {
        this.storeItemSizeId = storeItemSizeId;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    public long getCount() {
        return count;
    }
    public void setCount(long count) {
        this.count = count;
    }
    public StoreItemSizeEnum getSize() {
        return size;
    }
    public void setSize(StoreItemSizeEnum size) {
        this.size = size;
    }
    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    
}