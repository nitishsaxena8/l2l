package com.xperience.aem.xpbootstrap.core.component.internal;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import com.xperience.aem.xpbootstrap.core.component.Utils.LinkManager;
import com.xperience.aem.xpbootstrap.core.component.models.LayoutContainer;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

/**
 * Layout container model implementation.
 */
@Model(adaptables = SlingHttpServletRequest.class, adapters = LayoutContainer.class, resourceType = LayoutContainerImpl.RESOURCE_TYPE_V1)
public class LayoutContainerImpl extends AbstractContainerImpl implements LayoutContainer {

	private static final Logger logger = LoggerFactory.getLogger(LayoutContainerImpl.class);
    
	
    /**
     * The resource type.
     */
    protected static final String RESOURCE_TYPE_V1 = "xpbootstrap/components/master/organisms/xp-container";

    /**
     * The current resource.
     */
    @ScriptVariable
    private Resource resource;

    @Self
    LinkManager linkManager;
    /**
     * The layout type.
     */
    private LayoutType layout;

    /**
     * The accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String accessibilityLabel;

    /**
     * The role attribute.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String roleAttribute;

    /**
     * Initialize the model.
     */
    @PostConstruct
    protected void initModel() {
        // Note: this can be simplified using Optional.or() in JDK 11
        this.layout = Optional.ofNullable(
                        Optional.ofNullable(resource.getValueMap().get(LayoutContainer.PN_LAYOUT, String.class))
                                .orElseGet(() -> Optional.ofNullable(currentStyle)
                                        .map(style -> currentStyle.get(LayoutContainer.PN_LAYOUT, String.class))
                                        .orElse(null)
                                ))
                .map(LayoutType::getLayoutType)
                .orElse(LayoutType.SIMPLE);
    }

    @Override
    @NotNull
    protected List<ResourceListItemImpl> readItems() {
    	logger.info("reading items via LayoutContainerImpl");
        return getChildren().stream()
                .map(res -> new ResourceListItemImpl(linkManager, res, getId(), component))
                .collect(Collectors.toList());
    }

    @Override
    public String[] getDataLayerShownItems() {
        return null;
    }
}





