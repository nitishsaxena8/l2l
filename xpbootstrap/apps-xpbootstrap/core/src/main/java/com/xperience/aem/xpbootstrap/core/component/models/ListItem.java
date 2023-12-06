package com.xperience.aem.xpbootstrap.core.component.models;

import java.util.Calendar;
import java.util.List;


import com.day.cq.wcm.foundation.Search;
import com.drew.lang.annotations.Nullable;
import com.xperience.aem.xpbootstrap.core.component.Utils.Link;
import org.apache.sling.api.resource.Resource;
import org.osgi.annotation.versioning.ConsumerType;



/**
 * Interface for a generic list item, used by the {@link List} and {@link Search} models.
 *
 * @since com.adobe.cq.wcm.core.components.models 12.2.0
 */
@ConsumerType
public interface ListItem extends Component {

    /**
     * Returns the link of this {@code ListItem}.
     *
     * @return the link of this list item.
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    @Nullable
    default Link getLink() {
        return null;
    }

    /**
     * Returns the URL of this {@code ListItem}.
     *
     * @return the URL of this list item or {@code null}
     * @since com.adobe.cq.wcm.core.components.models 12.2.0
     * @deprecated Please use {@link #getLink()}
     */
    @Deprecated
    @Nullable
    default String getURL() {
        return null;
    }

    /**
     * Returns the title of this {@code ListItem}.
     *
     * @return the title of this list item or {@code null}
     * @since com.adobe.cq.wcm.core.components.models 12.2.0
     */
    @Nullable
    default String getTitle() {
        return null;
    }

    /**
     * Returns the description of this {@code ListItem}.
     *
     * @return the description of this list item or {@code null}
     * @since com.adobe.cq.wcm.core.components.models 12.2.0
     */
    @Nullable
    default String getDescription() {
        return null;
    }

    /**
     * Returns the date when this {@code ListItem} was last modified.
     *
     * @return the last modified date of this list item or {@code null}
     * @since com.adobe.cq.wcm.core.components.models 12.2.0
     */
    @Nullable
    default Calendar getLastModified() {
        return null;
    }

    /**
     * Returns the path of this {@code ListItem}.
     *
     * @return the list item path or {@code null}
     * @since com.adobe.cq.wcm.core.components.models 12.2.0
     */
    @Nullable
    default String getPath() {
        return null;
    }

    /**
     * Returns the name of this {@code ListItem}.
     *
     * @return the list item name or {@code null}
     * @since com.adobe.cq.wcm.core.components.models 12.6.0
     */
    @Nullable
    default String getName() {
        return null;
    }

    /**
     * Returns a wrapped resource of the item which is used to render the item as a Teaser component.
     *
     * The wrapped resource is either:
     * - the featured image of the item page, if it exists
     * - the content node of the item page, if it exists
     * - null otherwise
     *
     * @return wrapped resource of the item which can be rendered as a Teaser component
     * @since com.adobe.cq.wcm.core.components.models 12.21.0
     */
    @Nullable
    default Resource getTeaserResource() { return null;}

}
