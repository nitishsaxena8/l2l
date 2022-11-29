(function(channel) {
    "use strict";

    channel.on("cq-editor-loaded", function(event) {
        if (window.CQ && window.CQ.CoreComponents && window.CQ.CoreComponents.panelcontainer &&
            window.CQ.CoreComponents.panelcontainer.v1 && window.CQ.CoreComponents.panelcontainer.v1.registry) {
            window.CQ.CoreComponents.panelcontainer.v1.registry.register({
                name: "cmp-tabs",
                selector: ".cmp-tabs",
                wrapperSelector: '[data-panelcontainer="tabs"]',
                itemSelector: "[data-cmp-hook-tabs='tabpanel']",
                itemActiveSelector: ".cmp-tabs__tabpanel--active"
            });
        }
    });

})(jQuery(document));