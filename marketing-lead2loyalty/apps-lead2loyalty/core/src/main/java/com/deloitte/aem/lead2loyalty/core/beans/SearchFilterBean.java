package com.deloitte.aem.lead2loyalty.core.beans;

public class SearchFilterBean {
    private String filterTitle;
    private long filterCount;

    public String getFilterTitle() {
        return filterTitle;
    }

    public void setFilterTitle(String filterTitle) {
        this.filterTitle = filterTitle;
    }

    public long getFilterCount() {
        return filterCount;
    }
    public void setFilterCount(long filterCount) {
        this.filterCount = filterCount;
    }

    public SearchFilterBean(String filterTitle, long filterCount) {
        this.filterTitle = filterTitle;
        this.filterCount = filterCount;
    }

    public SearchFilterBean() {
    }
}
