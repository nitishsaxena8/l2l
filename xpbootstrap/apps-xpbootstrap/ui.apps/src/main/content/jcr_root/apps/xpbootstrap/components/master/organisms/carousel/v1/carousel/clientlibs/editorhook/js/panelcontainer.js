
(function(channel) {
    "use strict";

    channel.on("cq-editor-loaded", function(event) {
        if (window.CQ && window.CQ.CoreComponents && window.CQ.CoreComponents.panelcontainer &&
            window.CQ.CoreComponents.panelcontainer.v1 && window.CQ.CoreComponents.panelcontainer.v1.registry) {
            window.CQ.CoreComponents.panelcontainer.v1.registry.register({
                name: "cmp-carousel",
                selector: ".cmp-carousel",
                itemSelector: "[data-cmp-hook-carousel='item']",
                itemActiveSelector: ".cmp-carousel__item--active"
            });
        }
    });

})(jQuery(document));
