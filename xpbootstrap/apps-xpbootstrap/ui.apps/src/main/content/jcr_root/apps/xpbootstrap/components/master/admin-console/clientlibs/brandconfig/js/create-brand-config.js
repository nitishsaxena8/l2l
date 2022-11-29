// jshint ignore: start
(function (document, $) {
"use strict";
    function buildForm() {
        return `
        <form class="coral-Form coral-Form--vertical">
            <div class="foundation-layout-util-vmargin">
                <div class="coral-FixedColumn foundation-layout-util-vmargin">
                    <div class="coral-FixedColumn-column">
                        <div class="coral-Form-fieldwrapper">
                            <label class="coral-Form-fieldlabel" id="titleLabel" for="title">Title</label>
                            <input class="coral-Form-field coral3-Textfield title" name="./jcr:content/jcr:title" type="text" labelledby="titleLabel" is="coral-textfield" id="title">
                        </div>
                        <div class="coral-Form-fieldwrapper">
                            <label class="coral-Form-fieldlabel" id="nameLabel" for="name">Name</label>
                            <input class="coral-Form-field coral3-Textfield name" type="text" labelledby="nameLabel" is="coral-textfield" id="name" data-foundation-validation="admin.pagename">
                        </div>
                    </div>
                </div>
            </div>
        </form>
        `;
    }

    function buildHeader(headerText) {
        return `
            <coral-dialog-header>
                <div class="cq-dialog-actions u-coral-pullRight">
                    <button is="coral-button" type="button" variant="minimal" coral-close="" title="Cancel" size="M" class="coral3-Button coral3-Button--minimal cq-dialog-cancel"><coral-icon icon="close" size="S" class="coral3-Icon coral3-Icon--close coral3-Icon--sizeS" role="img" aria-label="close"></coral-icon><coral-button-label></coral-button-label></button>
                    <button is="coral-button" variant="minimal" class="cq-dialog-header-action cq-dialog-submit coral3-Button coral3-Button--minimal" title="Done" size="M"><coral-icon icon="check" size="S" class="coral3-Icon coral3-Icon--check coral3-Icon--sizeS" role="img" aria-label="check"></coral-icon><coral-button-label></coral-button-label></button>
                </div>
                <span class="dialog-label">${headerText}</span>
            </coral-dialog-header>
        `;
    }
    
    function finishCreation(path) {
        var dialog = new Coral.Dialog().set({
            id: 'createBrandConfigDialog',
            header: {
                innerHTML: buildHeader("Brand configuration page created")
            },
            content: {
                innerHTML: "Your brand configuration page is created.  If you would like to edit the page now, click the confirm button."
            },
            footer: {
                innerHTML: ''
            },
            backdrop: "modal"
        });
        document.body.appendChild(dialog);
        $(dialog).find(".cq-dialog-cancel").click(()=>location.reload());
        $(dialog).find(".cq-dialog-submit").click(()=>location.href = `/editor.html${path}.html`);

        dialog.show();
        return false;        
    }
    
    function submitForm(dialog) {
        var path = '/conf/xpbootstrap/adminconsole';
        var form = $(dialog).find("form");
        var name = form.find(".name").val();
        var title = form.find(".title").val();
        var pageTitle = title.replaceAll(' ', '-').toLowerCase();

        var listPath = `${path}/${pageTitle}`;
        if ($(dialog).find("[invalid]").size() > 0) {
            return false;
        }

		if (title !== '') {
			$.ajax({
				method: 'post',
				url: listPath,
				cache: false,
				dataType: 'json',
				data: {
					"./jcr:primaryType": "cq:Page",
					"./jcr:content/jcr:primaryType": "nt:unstructured",
					"./jcr:content/sling:resourceType": "xpbootstrap/components/content/organisms/brandconfig",
					"./jcr:content/cq:template": "/conf/xpbootstrap/settings/wcm/templates/brandconfig",
					"./jcr:content/jcr:title": title,
					"./jcr:content/jcr:name": name


				},
				success: () => finishCreation(listPath),
				error: (err) => alert(err),
				complete: () => dialog.hide()
			});
		} else {
			alert('Please enter title')
		}       
    }

    function showCreateListDialog() {
        var dialog = new Coral.Dialog().set({
            id: 'createBrandConfigDialog',
            header: {
                innerHTML: buildHeader("ADLC : Create Brand Config Page")
            },
            content: {
                innerHTML: buildForm()
            },
            footer: {
                innerHTML: ''
            },
            backdrop: "modal"
        });
        document.body.appendChild(dialog);
        $(dialog).find(".cq-dialog-submit").click(()=>submitForm(dialog));
        
        dialog.show();
        return false;
    }

    function init() {
        $(".cq-siteadmin-admin-create-adlcbrandconfig").click(showCreateListDialog);
    }

    $(document).on("foundation-contentloaded", function (_) {
        init();
    });
})(document, Granite.$);