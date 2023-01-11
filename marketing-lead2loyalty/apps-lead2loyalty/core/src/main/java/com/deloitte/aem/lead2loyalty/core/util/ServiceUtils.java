package com.deloitte.aem.lead2loyalty.core.util;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.settings.SlingSettingsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;

/**
 * Assorted mix of helper methods.
 *
 * @author imrankhan5
 *
 */
public class ServiceUtils {

    private static final Logger log = LoggerFactory.getLogger(ServiceUtils.class);

    /**
     * Append the html extension to a {@code url}.
     *
     * @param resolver ResourceResolver
     * @param url      URL to evaluate
     * @return URL with extension (if needed)
     */
    @Nullable
    public static String getLink(ResourceResolver resolver,
        String url, SlingSettingsService slingSettingsService) {
        if (url == null) return null;

        Resource resource = resolver.getResource(url);
        String result = url;
        if (resource != null) {
            Page page = resource.adaptTo(Page.class);
            if (page != null) {
                result = url + ".html";
            }
        }

        if(slingSettingsService.getRunModes().contains("author")) {
            result = result + "?wcmmode=disabled";
        }

        log.trace("Append link extension: {} -> {}", url, result);
        return result;
    }

}