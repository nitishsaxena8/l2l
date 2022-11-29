package com.xperience.aem.xpbootstrap.core.component.Utils;


/**
 * A service that allows finding providers and embedding information for URLs.
 *
 * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
 */
public interface OEmbedClient {

    /**
     * Gets a suitable oEmbed provider for the given URL.
     *
     * @param url The URL
     * @return The name of the oEmbed provider, as defined in configuration. {@code null} if no provider is found
     *
     * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
     */
    String getProvider(String url);

    /**
     * Gets the embedding information from the oEmbed provider.
     *
     * @param url The URL to retrieve embedding information for
     * @return The oEmbed response, {@code null} otherwise
     *
     * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
     */
    OEmbedResponse getResponse(String url);

    /**
     * Determines whether the provider response HTML is allowed to be displayed in an unsafe context.
     *
     * @param url The URL to retrieve the unsafe context flag for
     * @return {@code true} if the provider response HTML is allowed to be displayed in an unsafe context,
     * {@code false} otherwise
     *
     * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
     */
    boolean isUnsafeContext(String url);
}