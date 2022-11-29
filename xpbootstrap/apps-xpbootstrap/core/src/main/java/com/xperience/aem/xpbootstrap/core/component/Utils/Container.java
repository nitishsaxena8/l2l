package com.xperience.aem.xpbootstrap.core.component.Utils;


import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.xperience.aem.xpbootstrap.core.component.models.Component;
import com.xperience.aem.xpbootstrap.core.component.models.ListItem;
import org.osgi.annotation.versioning.ConsumerType;

import com.adobe.cq.export.json.ComponentExporter;
import com.adobe.cq.export.json.ContainerExporter;
import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

/**
 * A base interface to be extended by containers such as the {Carousel}, {Tabs} and {Accordion} models.
 *
 * @since com.adobe.cq.wcm.core.components.models 12.5.0
 */
@ConsumerType
public interface Container extends Component, ContainerExporter {

    /**
     * Name of the configuration policy property that indicates if background images are enabled
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_BACKGROUND_IMAGE_ENABLED = "backgroundImageEnabled";

    /**
     * Name of the configuration policy property that indicates if background colors are enabled
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_BACKGROUND_COLOR_ENABLED = "backgroundColorEnabled";

    /**
     * Name of the configuration policy property that indicates if background colors are to be restricted to predefined values
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_BACKGROUND_COLOR_SWATCHES_ONLY = "backgroundColorSwatchesOnly";

    /**
     * Name of the resource property that indicates that path to the background image
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_BACKGROUND_IMAGE_REFERENCE = "backgroundImageReference";

    /**
     * Name of the resource property that indicates the background color
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_BACKGROUND_COLOR = "backgroundColor";

    /**
     * Returns a list of container items
     *
     * @return List of container items
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    @NotNull
    default List<ListItem> getItems() {
        return Collections.emptyList();
    }

    /**
     * Returns the background CSS style to be applied to the component's root element
     *
     * @return CSS style string for the component's root element
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    @Nullable
    default String getBackgroundStyle() {
        return null;
    }

    /**
     * @see ContainerExporter#getExportedItems()
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    @NotNull
    @Override
    default Map<String, ? extends ComponentExporter> getExportedItems() {
        return Collections.emptyMap();
    }

    /**
     * @see ContainerExporter#getExportedItemsOrder()
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    @NotNull
    @Override
    default String[] getExportedItemsOrder() {
        return new String[]{};
    }
}