
(function(window, document, $, Granite) {
    "use strict";

    /**
     * Handler to show/hide the MSM action buttons according to the selected tab.
     */
    $(document).on("coral-panelstack:change", ".cq-siteadmin-admin-properties-tabs", function(e) {
        var $target = $(e.target.selectedItem);

        var $actionBar = $("coral-actionbar");

        if ($target.find(".cq-siteadmin-admin-properties-blueprint").length > 0) {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-blueprint").removeClass("hide");
        } else {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-blueprint").addClass("hide");
        }

        if ($target.find(".cq-siteadmin-admin-properties-livecopy").length > 0) {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-livecopy").removeClass("hide");
        } else {
            $actionBar.find(".cq-siteadmin-admin-properties-actions-livecopy").addClass("hide");
        }

        if ($target.find(".js-cq-sites-PermissionsProperties").length > 0) {
            $actionBar.find(".js-cq-sites-PermissionsProperties-action").removeClass("hide");
        } else {
            $actionBar.find(".js-cq-sites-PermissionsProperties-action").addClass("hide");
        }
    });

})(window, document, Granite.$, Granite);
