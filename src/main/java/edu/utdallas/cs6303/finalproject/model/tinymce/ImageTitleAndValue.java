package edu.utdallas.cs6303.finalproject.model.tinymce;

/**
 * ImageTitleAndValue
 */
public class ImageTitleAndValue {

    private String title;
    private String value;

    public ImageTitleAndValue() { }

    public ImageTitleAndValue(String title, String value) {
        this.title = title;
        this.value = value;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
