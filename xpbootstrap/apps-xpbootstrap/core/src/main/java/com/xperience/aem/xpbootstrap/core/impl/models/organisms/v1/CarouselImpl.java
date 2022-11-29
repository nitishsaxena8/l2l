package com.xperience.aem.xpbootstrap.core.impl.models.organisms.v1;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import com.xperience.aem.xpbootstrap.core.component.internal.PanelContainerImpl;
import com.xperience.aem.xpbootstrap.core.models.organisms.Carousel;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.designer.Style;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentData;
import com.xperience.aem.xpbootstrap.core.component.Utils.Container;
import com.xperience.aem.xpbootstrap.core.component.models.Component;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;


import se.eris.notnull.Nullable;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = {Carousel.class, Component.class, Container.class},
        resourceType = CarouselImpl.RESOURCE_TYPE, defaultInjectionStrategy= DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME, extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class CarouselImpl extends PanelContainerImpl implements Carousel {

    /**
     * The resource type.
     */
    public static final String RESOURCE_TYPE = "xpbootstrap/components/master/organisms/carousel/v1/carousel";

    /**
     * The default slide transition delay in milliseconds.
     */
    protected static final Long DEFAULT_DELAY = 5000L;

    /**
     * The current resource properties.
     */
    @ScriptVariable
    protected ValueMap properties;

    /**
     * The accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected String accessibilityLabel;

    /**
     * The accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected String accessibilityPrevious;

    /**
     * The previous button accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected String accessibilityNext;

    /**
     * The next button accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected String accessibilityPlay;

    /**
     * The pause button accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected String accessibilityPause;

    /**
     * The play button accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected String accessibilityTablist;

    /**
     * Flag indicating if carousel item uses it's title for aria-label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    protected boolean accessibilityAutoItemTitles;

    /**
     * Flag indicating if autoplay is enabled.
     */
    protected boolean autoplay;

    /**
     * Time in milliseconds between slide transitions.
     */
    protected Long delay;

    /**
     * Flag indicating if auto-pause (on slide hover) is disabled.
     */
    protected boolean autopauseDisabled;

    private boolean controlsPrepended = false;

    /**
     * Initialize the model.
     */
    @PostConstruct
    protected void initModel() {
        Optional<Style> optionalStyle =  Optional.ofNullable(currentStyle);

        // get the autoplay value from the resource, or the style if not set, or default false if neither set
        autoplay = Optional.ofNullable(properties.get(PN_AUTOPLAY, Boolean.class))
                .orElseGet(() -> optionalStyle.map(style -> style.get(PN_AUTOPLAY, Boolean.class))
                        .orElse(false));

        // get the autoplay delay from the resource, or the style if not set, or default value if neither set
        delay = Optional.ofNullable(properties.get(PN_DELAY, Long.class))
                .orElseGet(() -> optionalStyle.map(style -> style.get(PN_DELAY, Long.class))
                        .orElse(DEFAULT_DELAY));

        // get the autopause disabled flag from the resource, or the style if not set, or false if neither set.
        autopauseDisabled = Optional.ofNullable(properties.get(PN_AUTOPAUSE_DISABLED, Boolean.class))
                .orElseGet(() -> optionalStyle.map(style -> style.get(PN_AUTOPAUSE_DISABLED, Boolean.class))
                        .orElse(false));

        controlsPrepended = optionalStyle.map(style -> style.get(PN_CONTROLS_PREPENDED, Boolean.class))
                .orElse(false);
    }

    @Override
    public boolean getAutoplay() {
        return autoplay;
    }

    @Override
    public Long getDelay() {
        return delay;
    }

    @Override
    public boolean getAutopauseDisabled() {
        return autopauseDisabled;
    }

    @Override
    @Nullable
    public String getAccessibilityLabel() {
        return accessibilityLabel;
    }

    @Override
    @Nullable
    public String getAccessibilityPrevious() {
        return accessibilityPrevious;
    }

    @Override
    @Nullable
    public String getAccessibilityNext() {
        return accessibilityNext;
    }

    @Override
    @Nullable
    public String getAccessibilityPlay() {
        return accessibilityPlay;
    }

    @Override
    @Nullable
    public String getAccessibilityPause() {
        return accessibilityPause;
    }

    @Override
    @Nullable
    public String getAccessibilityTablist() {
        return accessibilityTablist;
    }

    @Override
    @Nullable
    public boolean getAccessibilityAutoItemTitles() {
        return accessibilityAutoItemTitles;
    }

    @Override
    public boolean isControlsPrepended() {
        return controlsPrepended;
    }

    /*
     * DataLayerProvider implementation of field getters
     */

    @Override
    public String[] getDataLayerShownItems() {
        String[] shownItems = new String[0];
        List<ListItem> items = getItems();
        if (!items.isEmpty()) {
            ComponentData componentData = items.get(0).getData();
            if (componentData != null) {
                shownItems = new String[] {componentData.getId()};
            }
        }
        return shownItems;
    }
}

