package com.xperience.aem.xpbootstrap.core.models.organisms;


import com.xperience.aem.xpbootstrap.core.component.Utils.Container;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * Defines the {@code Tabs} Sling Model used for the {@code /apps/core/wcm/components/tabs} component.
 *
 * @since com.adobe.cq.wcm.core.components.models 12.5.0
 */
@ConsumerType
public interface Tabs extends AtmSpace,Container {

    /**
     * Returns the default active item
     *
     * @return The default active item
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    default String getActiveItem() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the tabs.
     *
     * @return an accessibility label for tabs
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityLabel() {
        return null;
    }
}