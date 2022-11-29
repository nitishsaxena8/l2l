package com.xperience.aem.xpbootstrap.core.component.internal;


import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.ComponentContext;
import com.xperience.aem.xpbootstrap.core.component.models.Component;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.*;
import org.osgi.annotation.versioning.ConsumerType;
import se.eris.notnull.Nullable;

/**
 * Abstract class that can be used as a base class for {@link Component} implementations.
 */
@ConsumerType
public  abstract class AbstractComponentImpl implements Component {
    @ValueMapValue
    @Optional
    private String marginTop;

    @ValueMapValue
    @Optional
    private String mobileMarginTop;

    @ValueMapValue
    @Optional
    private String marginBottom;

    @ValueMapValue
    @Optional
    private String mobileMarginBottom;
    /**
     * The current request.
     */
    @Self
    SlingHttpServletRequest request;

    /**
     * The current resource.
     */
    @SlingObject
    Resource resource;


    /**
     * The component.
     */
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    public
    com.day.cq.wcm.api.components.Component component;

    /**
     * The component context.
     */
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    ComponentContext componentContext;

    /**
     * The current page.
     */
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    Page currentPage;

    /**
     * The ID for this component.
     */
    private String id;

    /**
     * Flag indicating if the data layer is enabled.
     */
    private Boolean dataLayerEnabled;


    /**
     * Getter for current page.
     *
     * @return The current {@link Page}
     */
    Page getCurrentPage() {
        return currentPage;
    }

    /**
     * Setter for current page.
     *
     * @param currentPage The {@link Page} to set
     */
    void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }


    public String getMarginTop() {
        return marginTop;
    }

    public String getMobileMarginTop() {
        return mobileMarginTop;
    }

    public String getMarginBottom() {
        return marginBottom;
    }

    public String getMobileMarginBottom() {
        return mobileMarginBottom;
    }
}