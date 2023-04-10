package com.deloitte.aem.lead2loyalty.core.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Date;

public class SearchResultBean {
    private String title;
    private String description;
    private String imagePath;
    private String category;
    private String keywords;
    private String pagePath;
    @JsonIgnore
    private Date publishDate;
    private String publishDateAsString;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getPagePath() {
        return pagePath;
    }

    public void setPagePath(String pagePath) {
        this.pagePath = pagePath;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public String getPublishDateAsString() {
        return publishDateAsString;
    }

    public void setPublishDateAsString(String publishDateAsString) {
        this.publishDateAsString = publishDateAsString;
    }
}
