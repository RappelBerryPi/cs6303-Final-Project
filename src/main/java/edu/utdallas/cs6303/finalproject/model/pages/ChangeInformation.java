package edu.utdallas.cs6303.finalproject.model.pages;

import java.math.BigDecimal;

public class ChangeInformation {

    private BigDecimal itemSubtotal;

    private BigDecimal subtotal;

    private BigDecimal tax;

    private BigDecimal total;
    
    private long totalItemCount;

    public BigDecimal getItemSubtotal() {
        return itemSubtotal;
    }

    public long getTotalItemCount() {
        return totalItemCount;
    }

    public void setTotalItemCount(long totalItemCount) {
        this.totalItemCount = totalItemCount;
    }

    public void setItemSubtotal(BigDecimal itemSubtotal) {
        this.itemSubtotal = itemSubtotal;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    
}
