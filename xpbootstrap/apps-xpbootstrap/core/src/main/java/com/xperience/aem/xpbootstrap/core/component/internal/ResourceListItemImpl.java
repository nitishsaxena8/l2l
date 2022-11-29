package com.xperience.aem.xpbootstrap.core.component.internal;


import java.util.Calendar;
import java.util.Collection;
import java.util.Map;

import com.day.cq.wcm.api.components.ComponentEditConfig;
import com.day.cq.wcm.api.components.VirtualComponent;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.components.Component;
import se.eris.notnull.NotNull;

/**
 * Resource-backed list item implementation.
 */
public class ResourceListItemImpl extends AbstractListItemImpl implements ListItem {

	private static final Logger logger = LoggerFactory.getLogger(ResourceListItemImpl.class);
    
	protected Link link;
    /**
     * The title.
     */
    protected String title;

    /**
     * The description.
     */
    protected String description;

    /**
     * The last modified date.
     */
    protected Calendar lastModified;

    /**
     * The name.
     */
    protected String name;

    /**
     * Construct a resource-backed list item.
     *
     * @param resource The resource.
     * @param parentId The ID of the containing component.
     */
    public ResourceListItemImpl(@NotNull LinkManager linkManager, @NotNull Resource resource,
                                String parentId, Component component) {
        super(parentId, resource, component);
        ValueMap valueMap = resource.getValueMap();
        title = valueMap.get(JcrConstants.JCR_TITLE, String.class);
        description = valueMap.get(JcrConstants.JCR_DESCRIPTION, String.class);
        lastModified = valueMap.get(JcrConstants.JCR_LASTMODIFIED, Calendar.class);
        path = resource.getPath();
        name = resource.getName();
        link = linkManager.get(resource).build();
        
        logger.debug("Resource description : {}", description);
		logger.debug("Resource link : {}", link);
		logger.debug("Resource name : {}", name);
		logger.debug("Resource Path : {}", path);
		logger.debug("Resource title : {}", title);
        
    }

	public Link getLink() {
		return link;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Calendar getLastModified() {
		return lastModified;
	}

	public String getName() {
		return name;
	}


	@Override
	public String getPath() {
		return path;
	}
}
