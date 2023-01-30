$( document ).ready(function() {
	$( ".quote-submit-btn button" ).on( "click", function() {

	    $("#guideContainerForm input").change(function() { 
             $('.form-success-container').addClass('d-none'); 
        });     $('.request-quote-btn').click(function(event) {
             $('.form-success-container').addClass('d-none'); 
        });

        var quote = {};
        $('#guideContainerForm').find("div.guideFieldNode").each(function(){
            var name, value;

            var classList = $(this).attr("class");
            var classArr = classList.split(/\s+/);
            name = classArr[2];

            if($(this).find("input").length != 0) {
                $(this).find("input").each(function() {
                    value = $(this).val();
                    console.log(value);
                });
            }

            if($(this).find("select").length != 0) {
                value = $(this).find("select option:selected").text();
            }

            if(value === 'undefined' || value === null ) {
                return false;
            }

            quote[name] = value;
        });

		if ($('.product-page-details', parent.document).attr('product-id') !== 'undefined') {
			quote["productId"] = $('.product-page-details', parent.document).attr("product-id");
        }

        if ($('.product-page-details', parent.document).attr("product-title") !== 'undefined') {
            quote["productTitle"] = $('.product-page-details', parent.document).attr("product-title");
        }

        if ($('.product-page-details', parent.document).attr("product-description") !== 'undefined') {
            quote["productDescription"] = $('.product-page-details', parent.document).attr("product-description");
        }

        if ($('.product-page-details', parent.document).attr("product-path") !== 'undefined') {
            quote["productPath"] = $('.product-page-details', parent.document).attr("product-path");
        }

        console.log(quote);

        MktoForms2.whenReady(function(form){ 
            form.addHiddenFields(quote);
            form.submit();

            form.onSuccess(function(vals,thanksURL){
            $('.form-success-container', parent.document).removeClass('d-none'); 
                return false;
            });
        });

        if ($('.product-page-details', parent.document) && $('.product-page-details', parent.document).attr("product-title")) {
            $('#requestAQuotemdelTitle', parent.document).text("Request a Quote - "+$('.product-page-details', parent.document).attr("product-title"));
        }

    });
});