package com.xperience.aem.xpbootstrap.core.component.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentData;
import com.xperience.aem.xpbootstrap.core.component.Utils.ComponentDataModelSerializer;
import org.osgi.annotation.versioning.ConsumerType;
import se.eris.notnull.Nullable;

/**
 * A base interface to be extended by embeddable
 *
 * @since com.adobe.cq.wcm.core.components.models.embeddable 1.1.0
 */
@ConsumerType
public interface Embeddable {
    /**
     * Returns the data layer information associated with the embeddable
     *
     * @return {@link ComponentData} object associated with the embeddable
     *
     * @since com.adobe.cq.wcm.core.components.models.embeddable 1.1.0
     */
    @Nullable
    @JsonProperty("dataLayer")
    @JsonSerialize(using = ComponentDataModelSerializer.class)
    default ComponentData getData() {
        return null;
    }
}
