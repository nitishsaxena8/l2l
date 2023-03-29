package com.deloitte.aem.lead2loyalty.core.beans;

import java.util.List;

public class SearchResponseWrapper {
    private List<SearchResultBean> searchResultBeanList;
    private int searchResultCount;
    private int pageCount;
    private List<SearchFilterBean> searchFilterBeanList;

    public List<SearchResultBean> getSearchResultBeanList() {
        return searchResultBeanList;
    }

    public void setSearchResultBeanList(List<SearchResultBean> searchResultBeanList) {
        this.searchResultBeanList = searchResultBeanList;
    }

    public int getSearchResultCount() {
        return searchResultCount;
    }

    public void setSearchResultCount(int searchResultCount) {
        this.searchResultCount = searchResultCount;
    }

    public int getPageCount() {
        return pageCount;
    }
    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }
    public List<SearchFilterBean> getSearchFilterBeanList() {
        return searchFilterBeanList;
    }
    public void setSearchFilterBeanList(List<SearchFilterBean> searchFilterBeanList) {
        this.searchFilterBeanList = searchFilterBeanList;
    }

}
