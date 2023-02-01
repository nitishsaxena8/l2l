package com.deloitte.aem.lead2loyalty.core.models;

import com.deloitte.aem.lead2loyalty.core.util.ServiceUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.settings.SlingSettingsService;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Objects;

@Model(adaptables = {Resource.class}, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class TileItem {

    @Inject
    private String productTitle;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private SlingSettingsService settingsService;

    @Inject
    private String productDescription;

    @Inject
    private String productImageSource;

    @Inject
    private String productPath;

    @Inject
    private String productScrollId;

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

    public String getProductPath() {
        if(StringUtils.isNotBlank(productScrollId) && StringUtils.isNotBlank(productPath))
            return Objects.requireNonNull(ServiceUtils.getLink(resourceResolver, productPath, settingsService)).concat("#").concat(productScrollId);
        else
            return ServiceUtils.getLink(resourceResolver, productPath, settingsService);
    }

    public void setProductPath(String productPath) {
        this.productPath = ServiceUtils.getLink(resourceResolver, productPath, settingsService);
    }

    public String getProductScrollId() {
        return productScrollId;
    }

    public void setProductScrollId(String productScrollId) {
        this.productScrollId = productScrollId;
    }
}