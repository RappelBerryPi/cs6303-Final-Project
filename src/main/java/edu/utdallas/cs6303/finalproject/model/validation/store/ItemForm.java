package edu.utdallas.cs6303.finalproject.model.validation.store;

import java.util.Date;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.springframework.format.annotation.DateTimeFormat;

import edu.utdallas.cs6303.finalproject.model.database.enums.StoreItemCategoryEnum;
import edu.utdallas.cs6303.finalproject.model.validation.explicittext.NotContainingExplicitText;
import edu.utdallas.cs6303.finalproject.model.validation.photo.PhotoPickIsValid;
import edu.utdallas.cs6303.finalproject.model.validation.safehtml.SafeHtml;

public class ItemForm {

    public static final String COST_REGEX = "^\\d{0,}(\\.\\d{1,2})?$";

    private long id;
    
    @NotNull
    @NotBlank
    @SafeHtml
    @NotContainingExplicitText
    private String name;

    @NotNull
    @NotBlank
    @Pattern(regexp = ItemForm.COST_REGEX)
    private String snackCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = ItemForm.COST_REGEX)
    private String xsCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = ItemForm.COST_REGEX)
    private String smallCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = ItemForm.COST_REGEX)
    private String mediumCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = ItemForm.COST_REGEX)
    private String largeCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = ItemForm.COST_REGEX)
    private String doubleXlCost;

    @NotNull
    @NotBlank
    @PhotoPickIsValid
    private String imageName;
    
    @NotNull
    @NotBlank
    @SafeHtml
    @NotContainingExplicitText
    private String shortDescription;

    @NotNull
    @NotBlank
    @SafeHtml
    @NotContainingExplicitText
    private String longDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date openDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date archiveDate;

    private StoreItemCategoryEnum category;

    private boolean alwaysAvailable;

    private boolean visible;

    private long amountInStock;

    public long getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
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

    public boolean isAlwaysAvailable() {
        return alwaysAvailable;
    }

    public void setAlwaysAvailable(boolean alwaysAvailable) {
        this.alwaysAvailable = alwaysAvailable;
    }

    @AssertTrue(message = "Must have a start or end date or be always available")
    public boolean isAlwaysAvailableOrHasAStartAndEndDate() {
        if (this.alwaysAvailable && this.openDate == null && this.archiveDate == null) {
            return true;
        }
        if (!this.alwaysAvailable && (this.openDate != null || this.archiveDate != null)) {
            return true;
        }
        return false;
    }

    public String getSnackCost() {
        return snackCost;
    }

    public void setSnackCost(String snackCost) {
        this.snackCost = snackCost;
    }

    public String getXsCost() {
        return xsCost;
    }

    public void setXsCost(String xsCost) {
        this.xsCost = xsCost;
    }

    public String getSmallCost() {
        return smallCost;
    }

    public void setSmallCost(String smallCost) {
        this.smallCost = smallCost;
    }

    public String getMediumCost() {
        return mediumCost;
    }

    public void setMediumCost(String mediumCost) {
        this.mediumCost = mediumCost;
    }

    public String getLargeCost() {
        return largeCost;
    }

    public void setLargeCost(String largeCost) {
        this.largeCost = largeCost;
    }

    public String getDoubleXlCost() {
        return doubleXlCost;
    }

    public void setDoubleXlCost(String doubleXlCost) {
        this.doubleXlCost = doubleXlCost;
    }

    public StoreItemCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(StoreItemCategoryEnum category) {
        this.category = category;
    }

}