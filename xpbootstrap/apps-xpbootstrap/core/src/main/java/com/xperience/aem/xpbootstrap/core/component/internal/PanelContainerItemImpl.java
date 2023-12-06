package com.xperience.aem.xpbootstrap.core.component.internal;

import java.util.Optional;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.components.Component;
import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;

import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

/**
 * Panel container item implementation.
 */
public class PanelContainerItemImpl extends ResourceListItemImpl implements ListItem {

	private static final Logger logger = LoggerFactory.getLogger(PanelContainerItemImpl.class);
    /**
     * The current page.
     */
    @ScriptVariable(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private Page currentPage;
    /**
     * Name of the property that contains the panel item's title.
     */
    public static final String PN_PANEL_TITLE = "cq:panelTitle";

    @Self
    public LinkManager linkManager;

    /**
     * Construct a panel item.
     *
     * @param resource The resource.
     * @param parentId The ID of the containing component.
     */
    public PanelContainerItemImpl(@NotNull final LinkManager linkManager, @NotNull final Resource resource, final String parentId, Component component,
                                  Page currentPage) {
        super(linkManager, resource, parentId, component);
        setCurrentPage(currentPage);
        title = Optional.ofNullable(resource.getValueMap().get(PN_PANEL_TITLE, String.class))
                .orElseGet(() -> resource.getValueMap().get(JcrConstants.JCR_TITLE, String.class));
        logger.debug("Resource Title : {}", title);
    }


    protected void setCurrentPage(Page currentPage) {
        this.currentPage = currentPage;
    }
}
