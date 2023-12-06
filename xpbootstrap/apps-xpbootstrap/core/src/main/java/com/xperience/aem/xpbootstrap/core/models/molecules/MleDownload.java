package com.xperience.aem.xpbootstrap.core.models.molecules;

import com.xperience.aem.xpbootstrap.core.component.models.Component;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmSpace;
import com.xperience.aem.xpbootstrap.core.models.atoms.AtmTitle;

public interface MleDownload extends  AtmTitle, AtmSpace, Component {

    /**
     * Name of the resource property that defines whether or not the title value is taken from the configured asset.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_TITLE_FROM_ASSET = "titleFromAsset";

    /**
     * Name of the resource property that defines whether or not the description value is taken from the configured asset.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_DESCRIPTION_FROM_ASSET = "descriptionFromAsset";

    /**
     * Name of the resource property that defines whether or not the download item should be displayed inline vs. attachment.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_INLINE = "inline";

    /**
     * Name of the policy property that defines the text to be displayed on the action.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_ACTION_TEXT = "actionText";

    /**
     * Name of the policy property that stores the value for this title's HTML element type.
     *
     * @see #getTitleType()
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_TITLE_TYPE = "titleType";

    /**
     * Name of the policy property that defines whether the file's size will be displayed.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_DISPLAY_SIZE = "displaySize";

    /**
     * Name of the policy property that defines whether the file's format will be displayed.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_DISPLAY_FORMAT = "displayFormat";

    /**
     * Name of the policy property that defines whether the filename will be displayed.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    String PN_DISPLAY_FILENAME = "displayFilename";

    /**
     * Name of the policy property that defines whether the title links should be hidden.
     *
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    String PN_HIDE_TITLE_LINK = "hideTitleLink";

    /**
     * Returns either the title configured in the dialog or the title of the DAM asset,
     * depending on the state of the titleFromAsset checkbox.
     *
     * @return the download title
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getTitle() {
        return null;
    }


    /**
     * Returns the url to the asset.
     *
     * @return the asset url
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getUrl() {
        return null;
    }


    /**
     * Returns the HTML element to be used for the title as defined in the component policy.
     *
     * @return the title header element type
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getTitleType() {
        return null;
    }

    /**
     * Returns the size of the file to be downloaded.
     *
     * @return the size of download file
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getSize() {
        return null;
    }

    /**
     * Returns the extension of file to be downloaded. Extension is mapped with the {@link org.apache.sling.commons.mime.MimeTypeService}
     * . If no mapping can be found the extension is extracted from the filename.
     *
     * @return the extension of the download file
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getExtension() {
        return null;
    }

    /**
     * Checks if the file size should be displayed.
     *
     * @return {@code true} if the size should be displayed, {@code false} otherwise
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default boolean displaySize() {
        return false;
    }

    /**
     * Returns the mime type of the file to be downloaded.
     *
     * @return the mime type of the download file
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getFormat() {
        return null;
    }

    /**
     * Checks if the file format should be displayed.
     *
     * @return {@code true} if the format should be displayed, {@code false} otherwise
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default boolean displayFormat() {
        return false;
    }

    /**
     * Returns the filename of the file to be downloaded.
     *
     * @return the filename of the download file
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default String getFilename() {
        return null;
    }

    /**
     * Checks if the filename should be displayed.
     *
     * @return {@code true} if the filename should be displayed, {@code false} otherwise
     * @since com.adobe.cq.wcm.core.components.models 12.8.0
     */
    default boolean displayFilename() {
        return false;
    }

    /**
     * Checks if the title link should be hidden.
     *
     * @return {@code true} if the title link should be hidden, {@code false} if it should be rendered
     * @since com.adobe.cq.wcm.core.components.models 12.20.0
     */
    default boolean hideTitleLink() {
        return false;
    }

    default String getIcon(){return null;}

}
