package com.xperience.aem.xpbootstrap.core.component.Utils;

import org.osgi.annotation.versioning.ConsumerType;
import se.eris.notnull.NotNull;
import se.eris.notnull.Nullable;

/**
 * Builds a link and uses the specified link properties.
 */
@ConsumerType
public interface LinkBuilder {

    /**
     * Uses the specified property name to read the link URL from.
     *
     * @param name The property name
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder withLinkUrlPropertyName(@NotNull String name);

    /**
     * Uses the specified HTML link target.
     *
     * @param target The link target
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder withLinkTarget(@NotNull String target);

    /**
     * Uses the specified HTML link attribute.
     *
     * @param name The attribute name
     * @param value The attribute value
     * @return {@link LinkBuilder}
     */
    @NotNull
    LinkBuilder withLinkAttribute(@NotNull String name, @Nullable String value);

    /**
     * Returns the resolved link.
     *
     * @return {@link Link}
     */
    @NotNull
    Link build();
}

