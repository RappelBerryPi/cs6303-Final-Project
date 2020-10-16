package edu.utdallas.cs6303.finalproject.model.database;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ForeignKey;
import javax.persistence.ManyToOne;

@Entity
public class StoreItem {
    @Id
    private long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "FK_StoreItem_FeaturedPhoto"))
    private UploadedFile image;
    
    private String shortDescription;

    @Lob
    @Column
    private String longDescription;

    //is active/ can purchase

    private ZonedDateTime openDate;

    private ZonedDateTime archiveDate;

    private boolean visible;

    private long amountInStock;
    
    private boolean deleted;

    public long getId() {
        return id;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public UploadedFile getImage() {
        return image;
    }

    public void setImage(UploadedFile image) {
        this.image = image;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    public ZonedDateTime getOpenDate() {
        return openDate;
    }

    public void setOpenDate(ZonedDateTime openDate) {
        this.openDate = openDate;
    }

    public ZonedDateTime getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(ZonedDateTime archiveDate) {
        this.archiveDate = archiveDate;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public long getAmountInStock() {
        return amountInStock;
    }

    public void setAmmountInStock(long amountInStock) {
        this.amountInStock = amountInStock;
    }

    public boolean isActive() {
        //TODO: base this off of open and close date amount in stock, and if visible also if deleted
        return true; 
    }
}
