package com.xperience.aem.xpbootstrap.core.component.internal;

import com.day.cq.commons.ImageResource;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.Utils.Utils;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;
import com.xperience.aem.xpbootstrap.core.impl.models.AbstractCompImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PageListItemImpl extends AbstractCompImpl implements ListItem {

	private static final Logger LOGGER = LoggerFactory.getLogger(PageListItemImpl.class);
	
	private static final String SUBTITLE = "subtitle";
	private static final String AUTHOR_NAME = "authorName";
	private static final String PUBLISH_DATE = "publishDate";
    /**
     * The page for this list item.
     */
    protected Page page;

    /**
     * The link for this list item.
     */
    protected Link link;

    /**
     * Prefix prepended to the item ID.
     */
    private static final String ITEM_ID_PREFIX = "item";

    private static final String PN_TEASER_DELEGATE = "teaserDelegate";

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

    private final Component component;
    private Resource teaserResource;
    private boolean showDescription;
    private boolean linkItems;

    /**
     * List of properties that should be inherited when delegating to the featured image of the page.
     */
    private Map<String, String> overriddenProperties = new HashMap<>();
    private List<String> hiddenProperties = new ArrayList<>();

    /**
     * Construct a list item for a given page.
     *
     * @param linkManager The link manager.
     * @param page        The current page.
     * @param parentId    The ID of the list containing this item.
     * @param component   The component containing this list item.
     */
    public PageListItemImpl(@NotNull final LinkManager linkManager,
                            @NotNull final Page page,
                            final String parentId,
                            final Component component) {
        //super(parentId, page.getContentResource(), component);
        this.parentId = parentId;
        this.resource = page.getContentResource();
        this.link = linkManager.get(page).build();
        if (this.link.isValid()) {
            this.page = (Page) link.getReference();
        } else {
            this.page = page;
        }

        if (this.resource != null) {
            this.path = this.resource.getPath();
        }
        this.component = component;

        if (this.component != null) {
            this.dataLayerType = component.getResourceType() + "/" + ITEM_ID_PREFIX;
        }
    }


    @Override
    @JsonIgnore
    @Nullable
    public Link<Page> getLink() {
        return link;
    }

    @Override
    public String getURL() {
        return link.getURL();
    }

    @Override
    public String getTitle() {
        return PageListItemImpl.getTitle(this.page);
    }

    /**
     * Gets the title of a page list item from a given page.
     * The list item title is derived from the page by selecting the first non-null value from the
     * following:
     * <ul>
     *     <li>{@link Page#getNavigationTitle()}</li>
     *     <li>{@link Page#getPageTitle()}</li>
     *     <li>{@link Page#getTitle()}</li>
     *     <li>{@link Page#getName()}</li>
     * </ul>
     *
     * @param page The page for which to get the title.
     * @return The list item title.
     */
    public static String getTitle(@NotNull final Page page) {
        String title = page.getNavigationTitle();
        if (title == null) {
            title = page.getPageTitle();
        }
        if (title == null) {
            title = page.getTitle();
        }
        if (title == null) {
            title = page.getName();
        }
        return title;
    }

    @Override
    @JsonIgnore
    public Resource getTeaserResource() {
        if (teaserResource == null && component != null) {
            String delegateResourceType = component.getProperties().get(PN_TEASER_DELEGATE, String.class);
            if (StringUtils.isEmpty(delegateResourceType)) {
                LOGGER.error("In order for list rendering delegation to work correctly you need to set up the teaserDelegate property on" +
                        " the {} component; its value has to point to the resource type of a teaser component.", component.getPath());
            } else {
                Resource resourceToBeWrapped = Utils.getFeaturedImage(page);
                if (resourceToBeWrapped != null) {
                    // use the page featured image and inherit properties from the page item
                    overriddenProperties.put(JcrConstants.JCR_TITLE, this.getTitle());
                    if (showDescription) {
                        overriddenProperties.put(JcrConstants.JCR_DESCRIPTION, this.getDescription());
                    }
                    if (linkItems) {
                        overriddenProperties.put(ImageResource.PN_LINK_URL, this.getPath());
                    }
                } else {
                    // use the page content node and inherit properties from the page item
                    resourceToBeWrapped = page.getContentResource();
                    if (resourceToBeWrapped == null) {
                        return null;
                    }
                    if (!showDescription) {
                        hiddenProperties.add(JcrConstants.JCR_DESCRIPTION);
                    }
                    if (linkItems) {
                        overriddenProperties.put(ImageResource.PN_LINK_URL, this.getPath());
                    }
                }
                teaserResource = new CoreResourceWrapper(resourceToBeWrapped, delegateResourceType, hiddenProperties, overriddenProperties);
            }
        }
        return teaserResource;
    }

    @Override
    public String getDescription() {
        return page.getDescription();
    }

    @Override
    public Calendar getLastModified() {
        return page.getLastModified();
    }

    @Override
    public String getPath() {
        return page.getPath();
    }

    @Override
    @JsonIgnore
    public String getName() {
        return page.getName();
    }
    /*----------------------------------*/


	public String getSubTitle() {
		return getPageProperty(SUBTITLE);
	}

	public String getAuthorName() {
		return getPageProperty(AUTHOR_NAME);
	}


	public String getPublishDate() {
		return getPageProperty(PUBLISH_DATE);
	}
	
	private String getPageProperty(final String pagePropertyName) {
		final ValueMap pageProperties = page.getProperties();
		if (pageProperties.containsKey(pagePropertyName)) {
			return pageProperties.get(pagePropertyName, String.class);
		}
		return null;
	}

}
