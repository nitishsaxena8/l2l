package com.xperience.aem.xpbootstrap.core.component.internal;

import com.xperience.aem.xpbootstrap.core.component.models.Embeddable;
import se.eris.notnull.Nullable;

import java.net.URISyntaxException;

public interface YouTube extends Embeddable {

    /**
     * Name of the resource property that defines the id of the YouTube video.
     */
    String PN_VIDEO_ID = "youtubeVideoId";

    /**
     * Name of the resource property that defines the width of the iFrame hosting the YouTube video.
     */
    String PN_WIDTH = "youtubeWidth";

    /**
     * Name of the resource property that defines the height of the iFrame hosting the YouTube video.
     */
    String PN_HEIGHT = "youtubeHeight";

    /**
     * Name of the resource property that defines the aspect ratio of the iFrame hosting the YouTube video.
     */
    String PN_ASPECT_RATIO = "youtubeAspectRatio";

    /**
     * Name of the resource property that defines the layout type of the youtube video.
     */
    String PN_LAYOUT = "layout";

    /* The following resource property names are used for optional YouTube player paramters */
    String PN_AUTOPLAY = "youtubeAutoPlay";

    String PN_MUTE = "youtubeMute";

    String PN_LOOP = "youtubeLoop";

    String PN_REL = "youtubeRel";

    String PN_PLAYS_INLINE = "youtubePlaysInline";

    String PN_DESIGN_MUTE_ENABLED = "youtubeMuteEnabled";

    String PN_DESIGN_MUTE_DEFAULT_VALUE = "youtubeMuteDefaultValue";

    String PN_DESIGN_AUTOPLAY_ENABLED = "youtubeAutoPlayEnabled";

    String PN_DESIGN_AUTOPLAY_DEFAULT_VALUE = "youtubeAutoPlayDefaultValue";

    String PN_DESIGN_LOOP_ENABLED = "youtubeLoopEnabled";

    String PN_DESIGN_LOOP_DEFAULT_VALUE = "youtubeLoopDefaultValue";

    String PN_DESIGN_RELATED_VIDEOS_ENABLED = "youtubeRelatedVideosEnabled";

    String PN_DESIGN_RELATED_VIDEOS_DEFAULT_VALUE = "youtubeRelatedVideosDefaultValue";

    String PN_DESIGN_PLAYS_INLINE_ENABLED = "youtubePlaysInlineEnabled";

    String  PN_DESIGN_PLAYS_INLINE_DEFAULT_VALUE = "youtubePlaysInlineDefaultValue";

    default @Nullable String getIFrameWidth() {
        return null;
    }

    default @Nullable String getIFrameHeight() {
        return null;
    }

    default @Nullable String getIFrameAspectRatio() {
        return null;
    }

    default @Nullable
    String getLayout() {
        return null;
    }

    default @Nullable String getIFrameSrc() throws URISyntaxException {
        return null;
    }

    default boolean isEmpty() {
        return false;
    }
}
