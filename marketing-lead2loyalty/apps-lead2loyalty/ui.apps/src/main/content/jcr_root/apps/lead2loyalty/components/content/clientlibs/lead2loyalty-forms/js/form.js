$( document ).ready(function() {

    if (window.location.href.indexOf("/signup") > -1) {
        $("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").removeClass("hidden");
    }

    if ($('.product-page-details', parent.document) && $('.product-page-details', parent.document).attr("product-title")) {
        $('#requestAQuotemdelTitle', parent.document).text("Request a Quote - "+$('.product-page-details', parent.document).attr("product-title"));
    }

	$( ".quote-submit-btn button" ).on( "click", function() {

        $('.signup-fail-container').addClass('d-none');
		$('.signup-fail-container').text('');
        $('.signup-success-container').addClass('d-none');
        $('.signup-success-container').text('');

        $("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldNode").attr("data-mandatory","true");


        if($("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find("input").val()) {
			$("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldNode").addClass("validation-success");
            $("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldNode").removeClass("validation-failure");
			$("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldError").text("");
        } else {
            $("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldNode").removeClass("validation-success");
			$("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldNode").addClass("validation-failure");
			$("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").find(".guideFieldError").text("This is a required field !!!");
        }


        if($("#guideContainer-rootPanel-afJsonSchemaRoot-password___guide-item").length > 0 && window.location.href.indexOf("/signup") > -1) {

			var loginData = collectFormData("signup");

            if(loginData != false) {

                $.ajax({
                  type: "POST",
                  url: "/bin/user",
                  data: JSON.stringify(loginData),
                  contentType: "application/json",
                  dataType: "json",
                  success: function(resultData) {
                      if (resultData && resultData.errorCode) {

                            $('.signup-fail-container', parent.document).removeClass('d-none');
                            $('.signup-fail-container', parent.document).text(resultData.errorMessage);

                            $('.signup-success-container', parent.document).text('');
                            $('.signup-success-container', parent.document).addClass('d-none');

                      } else {

                            $('.signup-success-container', parent.document).removeClass('d-none');
                            $('.signup-success-container').text("Thanks For Signing Up !!!");

                            $('.signup-fail-container', parent.document).text('');
                            $('.signup-fail-container', parent.document).addClass('d-none');


                            delete loginData.password;

                            MktoForms2.whenReady(function(form){ 
                                form.addHiddenFields(loginData);
                                form.submit();

                                form.onSuccess(function(vals,thanksURL){
                                    $('.form-success-container', parent.document).removeClass('d-none'); 
                                    //analytics
									parent.window.digitalData.event = 'formSubmission';
                                    parent.window.digitalData.user.authState = 'authenticated';
                                    parent.window.digitalData.form = {};
                                    parent.window.digitalData.form.formName = 'Signup';
                                    parent.window.digitalData.user = parent.window.digitalData.user || {};
                                    parent.window.digitalData.user.userType = 'member';
                                    parent.window.digitalData.user.email = loginData.Email;
                                    return false;
                                });
                            });

                      }
                  },
                  error: function(errorData) {

                    $('.signup-fail-container', parent.document).removeClass('d-none');
                    $('.signup-fail-container', parent.document).text("Something went wrong !!!");

                    $('.signup-success-container', parent.document).text('');
                    $('.signup-success-container', parent.document).addClass('d-none');
                  }
                });
            }
        } else {

            $("#guideContainerForm input").change(function() { 
                 $('.form-success-container').addClass('d-none'); 
            });     $('.request-quote-btn').click(function(event) {
                 $('.form-success-container').addClass('d-none'); 
            });


            var quote = collectFormData("quote");
            delete quote.submit;

            if(quote != false) {
            	callMarketoForm(quote);
            }
        }
    });

    function collectFormData(type) {
		var loginData = {};
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
                value = $(this).find("select option:selected").val();
            }

            if((value === '' || value === 'undefined' || value === null ) && $(this).attr("data-mandatory")) {
                if(name != "password") {
                    loginData = false;
                	return false;
                }
            }

            loginData[name] = value;
        });
        return loginData;
    }

    function callMarketoForm (loginData) {
		if ($('.product-page-details', parent.document).attr('product-id') !== 'undefined') {
            loginData["productId"] = $('.product-page-details', parent.document).attr("product-id");
        }

        if ($('.product-page-details', parent.document).attr("product-title") !== 'undefined') {
            loginData["productTitle"] = $('.product-page-details', parent.document).attr("product-title");
        }

        if ($('.product-page-details', parent.document).attr("product-description") !== 'undefined') {
            loginData["productDescription"] = $('.product-page-details', parent.document).attr("product-description");
        }

        if ($('.product-page-details', parent.document).attr("product-path") !== 'undefined') {
            loginData["productPath"] = $('.product-page-details', parent.document).attr("product-path");
        }

        delete loginData.password;

        console.log(loginData);

        MktoForms2.whenReady(function(form){ 
            form.addHiddenFields(loginData);
            form.submit();

            form.onSuccess(function(vals,thanksURL){
                $('.form-success-container', parent.document).removeClass('d-none'); 
                //analytics
                parent.window.digitalData.event = 'formSubmission';
                parent.window.digitalData.form = {};
                parent.window.digitalData.form.formName = 'Request Quote';
                return false;
            });
        });
    }
});