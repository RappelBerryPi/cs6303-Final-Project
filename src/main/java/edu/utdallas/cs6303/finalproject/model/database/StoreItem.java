package edu.utdallas.cs6303.finalproject.model.database;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
import javax.persistence.OneToMany;

import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;

@Entity
public class StoreItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @Column(precision = 16, scale = 2)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "image_id", foreignKey = @ForeignKey(name = "FK_StoreItem_FeaturedPhoto"))
    private UploadedFile image;
    
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    private StoreItemCategoryEnum category;

    @Lob
    @Column
    private String longDescription;

    private Date openDate;

    private Date archiveDate;

    private boolean visible;

    private long amountInStock;
    
    private boolean deleted;

    @OneToMany(mappedBy = "storeItem")
    private List<StoreItemSize> groupStoreItems;

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
        var retVal = false;
        if (this.isVisible() && !this.isDeleted() && this.openDateIsNullOrLessThanToday() && this.archiveDateIsNullOrGreaterThanToday()) {
            retVal = true;
        }
        return retVal;
    }

    private boolean openDateIsNullOrLessThanToday() {
        if (this.openDate == null) {
            return true;
        }
        if (this.openDate.compareTo(new Date()) <= 0) {
            return true;
        }
        return false;
    }

    private boolean archiveDateIsNullOrGreaterThanToday() {
        if (this.archiveDate == null) {
            return true;
        }
        if (this.archiveDate.compareTo(new Date()) > 0) {
            return true;
        }
        return false;
    }

    public StoreItemCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(StoreItemCategoryEnum category) {
        this.category = category;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public List<StoreItemSize> getGroupStoreItems() {
        return groupStoreItems.stream().sorted(Comparator.comparing(StoreItemSize::getSize)).collect(Collectors.toList());
    }

    public void setGroupStoreItems(List<StoreItemSize> groupStoreItems) {
        this.groupStoreItems = groupStoreItems;
    }

}
