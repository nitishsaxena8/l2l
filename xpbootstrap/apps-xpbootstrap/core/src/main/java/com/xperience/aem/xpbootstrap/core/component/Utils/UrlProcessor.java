package com.xperience.aem.xpbootstrap.core.component.Utils;

import java.util.Map;

/**
 * Interface that defines a generic processor for a given URL.
 *
 * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
 */
public interface UrlProcessor {

    /**
     * Returns the result of processing the given URL, {@code null} if processing is not possible or failed.
     *
     * @param url The URL to process
     * @return The {@link Result} of processing, {@code null} if processing is not possible or failed.
     * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
     */
    Result process(String url);

    /**
     * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
     */
    interface Result {

        /**
         * Returns the name of the processor that was able to process the URL.
         *
         * @return Name of the processor.
         * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
         */
        String getProcessor();

        /**
         * Returns the data from the processor that was able to process the URL.
         *
         * @return Data from the processor that was able to process the URL.
         * @since com.adobe.cq.wcm.core.components.services.embed 1.0.0
         */
        Map<String, Object> getOptions();
    }
}
