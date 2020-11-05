package edu.utdallas.cs6303.finalproject.model.pages;
import org.springframework.data.domain.Page;

public class ListPageInformation<T> {

    private Page<T> pageObject;
    private String  title;
    private String  editUrl;
    private String  viewUrl;
    private boolean isFirstPage;
    private boolean isLastPage;

    public Page<T> getPageObject() {
        return pageObject;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public void setPageObject(Page<T> pageObject) {
        this.pageObject = pageObject;
        this.isFirstPage = pageObject.getNumber() == 0;
        this.isLastPage = pageObject.getNumber() == (pageObject.getTotalPages() - 1);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEditUrl() {
        return editUrl;
    }

    public void setEditUrl(String editUrl) {
        this.editUrl = editUrl;
    }

    public boolean getIsFirstPage() {
        return isFirstPage;
    }

    public boolean getIsLastPage() {
        return isLastPage;
    }

    //conver to index 1 based for users.
    public int getFirstPageNumber() {
        return 1;
    }

    public int getPreviousPageNumber() {
        return pageObject.getNumber(); //getNumber is 0 index based
    }

    public int getCurrentPageNumber() {
        return pageObject.getNumber() + 1;
    }

    public int getNextPageNumber() {
        return pageObject.getNumber() + 2;
    }

    public int getLastPageNumber() {
        return pageObject.getTotalPages(); //index 1 based becase if there's only one page it returns 1.
    }

    public int getLength() {
        return pageObject.getSize();
    }

}