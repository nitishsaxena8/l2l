package com.xperience.aem.xpbootstrap.core.impl.models.organisms.v1;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import com.adobe.cq.export.json.ExporterConstants;
import com.day.cq.wcm.api.components.ComponentManager;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentData;
import com.xperience.aem.xpbootstrap.core.component.internal.PanelContainerImpl;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;
import com.xperience.aem.xpbootstrap.core.models.organisms.Tabs;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Exporter;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.InjectionStrategy;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import se.eris.notnull.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * Tabs model implementation.
 */
@Model(adaptables = SlingHttpServletRequest.class,
        adapters = {Tabs.class, ComponentExporter.class, ContainerExporter.class},
        resourceType = TabsImpl.RESOURCE_TYPE, defaultInjectionStrategy= DefaultInjectionStrategy.OPTIONAL)
@Exporter(name = ExporterConstants.SLING_MODEL_EXPORTER_NAME,
        extensions = ExporterConstants.SLING_MODEL_EXTENSION)
public class TabsImpl extends PanelContainerImpl implements Tabs {

    /**
     * The resource type.
     */
    public final static String RESOURCE_TYPE = "xpbootstrap/components/master/organisms/tabs/v1/tabs";


    /**
     * The current active tab.
     */
    @ValueMapValue
    @Nullable
    private String activeItem;




    public Resource getResource() {
        return resource;
    }

    /**
     * The accessibility label.
     */
    @ValueMapValue(injectionStrategy = InjectionStrategy.OPTIONAL)
    @Nullable
    private String accessibilityLabel;

    /**
     * The current resource.
     */
    @SlingObject
    private Resource resource;

    /**
     * The current request.
     */
    @Self
    SlingHttpServletRequest request;


    /**
     * The name of the active item.
     */
    private String activeItemName;

    @Override
    public String getActiveItem() {
        if (activeItemName == null) {
            this.activeItemName = Optional.ofNullable(this.activeItem)
                    .map(resource::getChild)
                    .map(Resource::getName)
                    .orElseGet(() ->
                            Optional.ofNullable(request.getResourceResolver().adaptTo(ComponentManager.class))
                                    .flatMap(componentManager -> StreamSupport.stream(resource.getChildren().spliterator(), false)
                                            .filter(Objects::nonNull)
                                            .filter(res -> Objects.nonNull(componentManager.getComponentOfResource(res)))
                                            .findFirst()
                                            .map(Resource::getName))
                                    .orElse(null));
        }
        return activeItemName;
    }

    @Nullable
    public String getAccessibilityLabel() {
        return accessibilityLabel;
    }

    /*
     * DataLayerProvider implementation of field getters
     */
    @Override
    public String[] getDataLayerShownItems() {
        String activeItemName = getActiveItem();
        List<ListItem> items = getItems();
        return items.stream()
                .filter(e -> StringUtils.equals(e.getName(), activeItemName))
                .findFirst()
                .map(ListItem::getData)
                .map(ComponentData::getId)
                .map(item -> new String[]{item})
                .orElseGet(() -> new String[0]);
    }
}