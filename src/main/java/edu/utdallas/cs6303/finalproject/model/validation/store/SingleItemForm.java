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

public class SingleItemForm {

    public static final String COST_REGEX = "^\\d{0,}(\\.\\d{1,2})?$";

    protected long id;
    
    @NotNull
    @NotBlank
    @SafeHtml
    @NotContainingExplicitText
    protected String name;

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String baseCost;

    @NotNull
    @NotBlank
    @PhotoPickIsValid
    protected String imageName;
    
    @NotNull
    @NotBlank
    @SafeHtml
    @NotContainingExplicitText
    protected String shortDescription;

    @NotNull
    @NotBlank
    @SafeHtml
    @NotContainingExplicitText
    protected String longDescription;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date openDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    protected Date archiveDate;

    protected StoreItemCategoryEnum category;

    protected boolean alwaysAvailable;

    protected boolean visible;

    protected long amountInStock;

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

    public StoreItemCategoryEnum getCategory() {
        return category;
    }

    public void setCategory(StoreItemCategoryEnum category) {
        this.category = category;
    }

    public String getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(String baseCost) {
        this.baseCost = baseCost;
    }

}