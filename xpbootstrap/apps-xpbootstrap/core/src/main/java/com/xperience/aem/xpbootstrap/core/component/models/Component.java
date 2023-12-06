package com.xperience.aem.xpbootstrap.core.component.models;

import com.adobe.cq.export.json.ComponentExporter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentData;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentDataModelSerializer;
import org.osgi.annotation.versioning.ConsumerType;
import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

/**
 * A base interface to be extended by components that need to provide access to common properties.
 *
 * @since com.adobe.cq.wcm.core.components.models 12.8.0
 */
@ConsumerType
public interface Component extends ComponentExporter {

    /**
     * Name of the resource property that indicates the HTML id for the component.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_ID = "id";

    /**
     * Returns the HTML id of the the component's root element
     *
     * @return HTML id of the component's root element
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    @Nullable
    default String getId() {
        return null;
    }

    /**
     * Returns the data layer information associated with the component
     *
     * @return {@linComponentData} object associated with the component
     *
     * @since com.adobe.cq.wcm.core.components.models 12.12.0
     */
    @Nullable
    @JsonProperty("dataLayer")
    @JsonSerialize(using = ComponentDataModelSerializer.class)
    default ComponentData getData() {
        return null;
    }

    /**
     * Returns the style system information associated with the component
     *
     * @return CSS classes selected by the content author delimited using a SPACE character
     *
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    @Nullable
    @JsonProperty("appliedCssClassNames")
    default String getAppliedCssClasses() {
        return null;
    }

    /**
     * @see ComponentExporter#getExportedType()
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    @NotNull
    @Override
    default String getExportedType() {
        return "";
    }

}
