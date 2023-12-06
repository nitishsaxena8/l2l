package com.xperience.aem.xpbootstrap.core.component.models;

/**
 * Interface for reading progressive web apps configuration
 */
public interface PWA {

    /**
     * Name of the property that defines if PWA is enabled for the website.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    final String PN_PWA_ENABLED = "pwaEnabled";

    /**
     * Name of the property that defines the start URL of this PWA
     *
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    final String PN_PWA_START_URL = "pwaStartURL";

    /**
     * Name of the property that defines the theme color used.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    final String PN_PWA_THEME_COLOR = "pwaThemeColor";

    /**
     * Name of the property that defines the PWA icon
     *
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    final String PN_PWA_ICON_PATH = "pwaIconPath";

    /**
     * Name of the manifest file that provides information about the PWA
     *
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    final String MANIFEST_NAME = "manifest.webmanifest";

    /**
     * Returns true if PWA features are enabled false otherwise
     *
     * @return whether PWA is enabled or not
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    default boolean isEnabled() {
        return false;
    }

    ;

    /**
     * The theme color of the site sometimes used to color the address bar of the browser
     *
     * @return A Hex String that represents the theme color for this site
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    default String getThemeColor() {
        return null;
    }

    ;

    /**
     * The path to the icon of this PWA
     *
     * @return A String that represents the relative path to the icon of this site
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    default String getIconPath() {
        return null;
    }

    ;

    /**
     * The relative path to the web manifest
     *
     * @return A String that represents the relative path to the web manifest of this site
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    default String getManifestPath() {
        return null;
    }

    /**
     * The relative path to the service worker
     *
     * @return A String that represents the path to the service worker used by this site
     * @since com.adobe.cq.wcm.core.components.models 12.19.0
     */
    default String getServiceWorkerPath() {
        return null;
    }

}

