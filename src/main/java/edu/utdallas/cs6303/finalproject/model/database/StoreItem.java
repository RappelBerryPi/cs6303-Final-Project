package edu.utdallas.cs6303.finalproject.model.database;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;

import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;

@Entity
public class StoreItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal snackCost;

    @Column(precision = 16, scale = 2)
    private BigDecimal xsCost;

    @Column(precision = 16, scale = 2)
    private BigDecimal smallCost;

    @Column(precision = 16, scale = 2)
    private BigDecimal mediumCost;

    @Column(precision = 16, scale = 2)
    private BigDecimal largeCost;

    @Column(precision = 16, scale = 2)
    private BigDecimal doubleXlCost;

    @ManyToOne
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "FK_StoreItem_FeaturedPhoto"))
    private UploadedFile image;
    
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    private StoreItemCategoryEnum category;

    @Lob
    @Column
    private String longDescription;

    //TODO: ?? is active/ can purchase

    private Date openDate;

    private Date archiveDate;

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

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public Date getArchiveDate() {
        return archiveDate;
    }

    public void setArchiveDate(Date archiveDate) {
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

    public void setAmountInStock(long amountInStock) {
        this.amountInStock = amountInStock;
    }

    public boolean isActive() {
        //TODO: base this off of open and close date amount in stock, and if visible also if deleted
        return true; 
    }

    public BigDecimal getSnackCost() {
        return snackCost;
    }

    public void setSnackCost(BigDecimal snackCost) {
        this.snackCost = snackCost;
    }

    public BigDecimal getXsCost() {
        return xsCost;
    }

    public void setXsCost(BigDecimal xsCost) {
        this.xsCost = xsCost;
    }

    public BigDecimal getSmallCost() {
        return smallCost;
    }

    public void setSmallCost(BigDecimal smallCost) {
        this.smallCost = smallCost;
    }

    public BigDecimal getMediumCost() {
        return mediumCost;
    }

    public void setMediumCost(BigDecimal mediumCost) {
        this.mediumCost = mediumCost;
    }

    public BigDecimal getLargeCost() {
        return largeCost;
    }

    public void setLargeCost(BigDecimal largeCost) {
        this.largeCost = largeCost;
    }

    public BigDecimal getDoubleXlCost() {
        return doubleXlCost;
    }

    public void setDoubleXlCost(BigDecimal doubleXlCost) {
        this.doubleXlCost = doubleXlCost;
    }

    public StoreItemCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(StoreItemCategoryEnum category) {
        this.category = category;
    }

}
