package com.deloitte.aem.lead2loyalty.core.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TileItem {

    @Inject
    private String productTitle;

    @Inject
    private String productDescription;


    @Inject
    private String productImageSource;

    @PostConstruct
    protected void init() {

    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }


    public String getProductImageSource() {
        return productImageSource;
    }

    public void setProductImageSource(String productImageSource) {
        this.productImageSource = productImageSource;
    }
}