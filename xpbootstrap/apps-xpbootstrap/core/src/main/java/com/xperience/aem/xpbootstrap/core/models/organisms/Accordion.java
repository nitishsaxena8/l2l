package com.xperience.aem.xpbootstrap.core.models.organisms;


import com.xperience.aem.xpbootstrap.core.component.Utils.Container;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Defines the {@code Accordion} Sling Model used for the {@code /apps/core/wcm/components/accordion} component.
 *
 * @since com.adobe.cq.wcm.core.components.models 12.8.0
 */
@ConsumerType
public interface Accordion extends AtmSpace,Container {

    /**
     * Name of the configuration policy property that stores the default value for the accordion heading's HTML element.
     *
     * @see #getHeadingElement()
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_DESIGN_HEADING_ELEMENT = "headingElement";

    /**
     * Indicates whether the accordion forces a single item to be expanded at a time or not.
     *
     * @return {@code true} if the accordion forces a single item to be expanded at a time; {@code false} otherwise
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default boolean isSingleExpansion() {
        return false;
    }

    /**
     * Returns the items that are expanded by default.
     *
     * @return the expanded items
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String[] getExpandedItems() {
        return null;
    }

    /**
     * Returns the HTML element to use for accordion headers.
     *
     * @return the HTML element to use for accordion headers
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getHeadingElement() {
        return null;
    }
}
