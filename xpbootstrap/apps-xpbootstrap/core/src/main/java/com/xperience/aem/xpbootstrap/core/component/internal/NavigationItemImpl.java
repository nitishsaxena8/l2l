package com.xperience.aem.xpbootstrap.core.component.internal;

import java.util.Collections;
import java.util.List;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.drew.lang.annotations.NotNull;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.NavigationItem;

public class NavigationItemImpl extends PageListItemImpl implements NavigationItem {

    protected List<NavigationItem> children = Collections.emptyList();
    protected int level;
    protected boolean active;
    private boolean current;

    public NavigationItemImpl(Page page, boolean active, boolean current, @NotNull LinkManager linkManager, int level,
                              List<NavigationItem> children,
                              String parentId, Component component) {
        super(linkManager, page, parentId, component);
        this.active = active;
        this.current = current;
        this.level = level;
        this.children = children;
    }

    @Override
    @JsonIgnore
    @Deprecated
    public Page getPage() {
        return page;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isCurrent() {
        return current;
    }

    @Override
    public List<NavigationItem> getChildren() {
        return children;
    }

    @Override
    public int getLevel() {
        return level;
    }
    
}
