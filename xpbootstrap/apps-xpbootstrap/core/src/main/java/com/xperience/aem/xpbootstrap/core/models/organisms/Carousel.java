package com.xperience.aem.xpbootstrap.core.models.organisms;


import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;
import org.osgi.annotation.versioning.ConsumerType;

import com.xperience.aem.xpbootstrap.core.component.Utils.Container;

@ConsumerType
public interface Carousel extends AtmSpace,Container {

    /**
     * Name of the resource property that indicates whether to automatically transition between slides, or not.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    String PN_AUTOPLAY = "autoplay";

    /**
     * Name of the resource property that indicates the delay (in milliseconds) when automatically transitioning between slides.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    String PN_DELAY = "delay";

    /**
     * Name of the resource property that indicates whether automatic pause on hovering the carousel is disabled, or not.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.6.0
     */
    String PN_AUTOPAUSE_DISABLED = "autopauseDisabled";

    /**
     * Name of the policy property that defines whether the control elements should be placed in front of the carousel items.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    String PN_CONTROLS_PREPENDED = "controlsPrepended";

    /**
     * Indicates whether the carousel should automatically transition between slides or not.
     *
     * @return {@code true} if the carousel should automatically transition slides; {@code false} otherwise
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    default boolean getAutoplay() {
        return false;
    }

    /**
     * Returns the delay (in milliseconds) when automatically transitioning between slides.
     *
     * @return the delay (in milliseconds) when automatically transitioning between slides
     * @since com.adobe.cq.wcm.core.components.models 12.5.0
     */
    default Long getDelay() {
        return null;
    }

    /**
     * Indicates whether automatic pause on hovering the carousel is disabled, or not.
     *
     * @return {@code true} if automatic pause on hovering the carousel should be disabled; {@code false} otherwise
     * @since com.adobe.cq.wcm.core.components.models 12.6.0
     */
    default boolean getAutopauseDisabled() {
        return false;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityLabel() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityPrevious() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityNext() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityPlay() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityPause() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default String getAccessibilityTablist() {
        return null;
    }

    /**
     * Returns an accessibility label that describes the carousel.
     *
     * @return an accessibility label for the carousel
     * @since com.adobe.cq.wcm.core.components.models 12.9.0
     */
    default boolean getAccessibilityAutoItemTitles() {
        return false;
    }

    /**
     * Checks if the control elements should be placed in front of the carousel items.
     *
     * @return {@code true} if the control elements should be placed in front of the items, {@code false} if they should be appended
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    default boolean isControlsPrepended() {
        return false;
    }
}