
/* global jQuery, Coral */
(function($) {
    "use strict";

    var selectors = {
        dialogContent: ".cmp-carousel__editor",
        autoplay: "[data-cmp-carousel-v1-dialog-hook='autoplay']",
        autoplayGroup: "[data-cmp-carousel-v1-dialog-hook='autoplayGroup']"
    };

    var autoplay;
    var autoplayGroup;

    $(document).on("dialog-loaded", function(event) {
        var $dialog = event.dialog;

        if ($dialog.length) {
            var dialogContent = $dialog[0].querySelector(selectors.dialogContent);

            if (dialogContent) {
                autoplay = dialogContent.querySelector(selectors.autoplay);
                autoplayGroup = dialogContent.querySelector(selectors.autoplayGroup);

                if (autoplay) {
                    Coral.commons.ready(autoplay, function() {
                        autoplay.on("change", onAutoplayChange);
                        onAutoplayChange();
                    });
                }
            }
        }
    });

    /**
     * Handles a change in the autoplay checkbox state.
     * Conditionally toggles hidden state of the related autoplay group which contains
     * additional fields that are only relevant when autoplay is enabled.
     *
     * @private
     */
    function onAutoplayChange() {
        if (autoplay && autoplayGroup) {
            if (!autoplay.checked) {
                autoplayGroup.setAttribute("hidden", true);
            } else {
                autoplayGroup.removeAttribute("hidden");
            }
        }
    }

})(jQuery);
