(function ($, ns, channel, window) {
    "use strict";
    var popover;
    var EditorFrame = ns.EditorFrame;
    var ACTION_ICON = "viewCard";
    var ACTION_TITLE = "View Selector";
    var ACTION_NAME = "VIEW_SELECTOR";
 
   /**
    * Represents the MYACTION action (opens a popover relative to the toolbar of the selected editable) that could be performed on the current {@link Granite.author.Editable}
    *
    * @memberOf Granite.author.edit.ToolbarActions
    * @type Granite.author.ui.ToolbarAction
    * @alias MYACTION
    */
 
    var ViewSelectorAction = new ns.ui.ToolbarAction ({
        name: ACTION_NAME,
        text: Granite.I18n.get(ACTION_TITLE),
        icon: ACTION_ICON,
        render: function ($el) {
            if (popover) {
                popover.target = $el[0];
            }
            return $el;
        },
        execute: function openEditDialog(editable) {
            editable.config.dialogSrc = "/mnt/override/apps/xpview/components/actions/viewselector/_cq_dialog.html" + editable.config.path;
            ns.DialogFrame.openDialog(new ns.edit.Dialog(editable));
        },
        condition: function (editable) {
            return !!(editable && editable.dom);
        },
        isNonMulti: true
    });
 
    channel.on('cq-layer-activated', function(event) {	
        popover = document.getElementById('viewSelector');
        if (event.layer === "Edit") {
            EditorFrame.editableToolbar.registerAction(ACTION_NAME, ViewSelectorAction);
        }
    });
 
}(jQuery, Granite.author, jQuery(document), this));