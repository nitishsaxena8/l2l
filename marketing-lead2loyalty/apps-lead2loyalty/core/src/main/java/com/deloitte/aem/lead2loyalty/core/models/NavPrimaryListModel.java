package com.deloitte.aem.lead2loyalty.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import java.util.List;

@Model(adaptables = { SlingHttpServletRequest.class,
        Resource.class }, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class NavPrimaryListModel {

    private String text;
    private String url;
    private String secondLevelMoreLink;

    public List<NavSecondaryListModel> secondaryLinks;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSecondLevelMoreLink() {
        return secondLevelMoreLink;
    }

    public void setSecondLevelMoreLink(String secondLevelMoreLink) {
        this.secondLevelMoreLink = secondLevelMoreLink;
    }

    public void setSecondaryLinks(List<NavSecondaryListModel> secondaryLinks) {
        this.secondaryLinks = secondaryLinks;
    }

    public List<NavSecondaryListModel> getSecondaryLinks() {
        return secondaryLinks;
    }

}
