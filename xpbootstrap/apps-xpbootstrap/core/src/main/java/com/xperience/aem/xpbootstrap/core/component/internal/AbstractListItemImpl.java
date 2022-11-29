package com.xperience.aem.xpbootstrap.core.component.internal;


import com.day.cq.wcm.api.components.Component;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentUtils;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import se.eris.notnull.NotNull;


/**
 * Abstract helper class for ListItem implementations.
 * Generates an ID for the item, using the ID of its parent as a prefix
 */
public abstract class AbstractListItemImpl extends AbstractComponentImpl implements ListItem {

    /**
     * Prefix prepended to the item ID.
     */
    private static final String ITEM_ID_PREFIX = "item";

    @Self
    LinkManager linkManager;
    /**
     * The ID of the component that contains this list item.
     */
    protected String parentId;

    /**
     * The path of this list item.
     */
    protected String path;

    /**
     * Data layer type.
     */
    protected String dataLayerType;

    @ScriptVariable
    Component component;

    /**
     * Construct a list item.
     *
     * @param parentId The ID of the containing component.
     * @param resource The resource of the list item.
     * @param component The component that contains this list item.
     */
    protected AbstractListItemImpl(String parentId, Resource resource, Component component) {
        this.parentId = parentId;
        this.component = component;
        if (resource != null) {
            this.path = resource.getPath();
        }
        if (component != null) {
            this.dataLayerType = component.getResourceType() + "/" + ITEM_ID_PREFIX;
        }
        this.resource = resource;
    }
    @NotNull

    public String getId() {
        return ComponentUtils.generateId(StringUtils.join(parentId, ComponentUtils.ID_SEPARATOR, ITEM_ID_PREFIX), path);
    }




}
