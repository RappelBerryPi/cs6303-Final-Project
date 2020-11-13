package edu.utdallas.cs6303.finalproject.model.validation.store;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class GroupItemForm extends SingleItemForm {

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String snackCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String xsCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String smallCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String mediumCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String largeCost;

    @NotNull
    @NotBlank
    @Pattern(regexp = COST_REGEX)
    protected String doubleXlCost;

    public GroupItemForm() {
        this.setBaseCost("0.00");
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

}