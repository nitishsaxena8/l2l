
(function(window, document, $) {
    "use strict";

    /**
     * Handler to enable/disable the brand slug textfield in the page properties dialog.
     */
    var brandSlugCheckboxSelector = 'coral-checkbox[name="./brandSlug_override"]';
    var brandSlugTextfieldSelector = 'input[name="./brandSlug"]';
    var brandSlugSectionSelector = "section.cq-siteadmin-admin-properties-basic-brandSlug";

    $(document).on("dialog-loaded", function(e) {
        var $dialog = e.dialog;
        var $brandSlugSection = $(brandSlugSectionSelector, $dialog);
        var $brandSlugCheckbox = $(brandSlugCheckboxSelector, $brandSlugSection);
        var $brandSlugTextfield = $(brandSlugTextfieldSelector, $brandSlugSection);

        if ($brandSlugCheckbox.length > 0 && $brandSlugTextfield.length > 0) {
            var inheritedValue = $brandSlugTextfield.data("inheritedValue");
            var specifiedValue = $brandSlugTextfield.data("specifiedValue");
            var textfieldFoundation = $brandSlugTextfield.adaptTo("foundation-field");
            var checkboxFoundation  = $brandSlugCheckbox.adaptTo("foundation-field");
            changeTextFieldState(textfieldFoundation, checkboxFoundation.getValue() === "true", inheritedValue, specifiedValue);
            $brandSlugCheckbox.on("change", function() {
                changeTextFieldState(textfieldFoundation, this.checked, inheritedValue, specifiedValue);
            });
        }
    });

    function changeTextFieldState(textfield, enabled, inheritedValue, specifiedValue) {
        if (enabled) {
            textfield.setDisabled(false);
            textfield.setValue(specifiedValue || "");
        } else {
            textfield.setDisabled(true);
            textfield.setValue(inheritedValue || "");
        }
    }

})(window, document, Granite.$);
