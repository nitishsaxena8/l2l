package com.xperience.aem.xpbootstrap.core.impl.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Optional;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.ComponentContext;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.xperience.aem.xpbootstrap.core.component.Utils.ContentFragmentUtils;
import com.xperience.aem.xpbootstrap.core.component.Utils.Utils;

public class AbstractCompImpl {

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
    
    @SlingObject
    protected Resource resource;
    
    @Self
    protected SlingHttpServletRequest request;
    
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected ComponentContext componentContext;
    
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected Page currentPage;
    
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected com.day.cq.wcm.api.components.Component component;
    
    private String id;

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

    @NotNull
    public String getId() {
        if (id == null) {
            String resourceCallerPath = (String)request.getAttribute(ContentFragmentUtils.ATTR_RESOURCE_CALLER_PATH);
            this.id = Utils.generateId(this.resource, this.currentPage, resourceCallerPath, this.componentContext);
        }
        return id;
    }

}
