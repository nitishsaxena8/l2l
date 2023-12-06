package com.xperience.aem.xpbootstrap.core.component.internal;

import static org.apache.sling.api.SlingConstants.PROPERTY_PATH;

import java.util.List;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.NavigationItem; 

@JsonIgnoreProperties(value = {"page", "children", "level", "description", "lastModified",PROPERTY_PATH})
public class BreadcrumbItemImpl extends NavigationItemImpl implements NavigationItem {

    public BreadcrumbItemImpl(Page page, boolean active, @NotNull LinkManager linkManager, int level,
                              List<NavigationItem> children, String parentId, Component component) {
        super(page, active, active, linkManager, level, children, parentId, component);
    }

    @Override
    @JsonIgnore(false)
    @Nullable
    public Link getLink() {
        return super.getLink();
    }
}
