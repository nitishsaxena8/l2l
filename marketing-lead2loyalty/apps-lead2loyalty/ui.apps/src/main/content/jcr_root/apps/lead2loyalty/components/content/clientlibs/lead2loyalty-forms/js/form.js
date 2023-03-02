$( document ).ready(function() {

    if ($('.product-page-details', parent.document) && $('.product-page-details', parent.document).attr("product-title")) {
        $('#requestAQuotemdelTitle', parent.document).text("Request a Quote - "+$('.product-page-details', parent.document).attr("product-title"));
    }

    $( ".signup-submit-btn button" ).on( "click", function() {

        $('.signup-fail-container').addClass('d-none');
		$('.signup-fail-container').text('');
        $('.signup-success-container').addClass('d-none');
        $('.signup-success-container').text('');

        $("#guideContainerForm input").change(function() { 
            $('.form-success-container').addClass('d-none'); 
        });     $('.request-quote-btn').click(function(event) {
            $('.form-success-container').addClass('d-none'); 
        });


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

                            MktoForms2.loadForm("//733-JCL-696.mktoweb.com", "733-JCL-696", 1001, function(form) {
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
    });

//Contact us
    $( ".contactus-submit-btn button" ).on( "click", function() {

        var contactUsFormData = collectFormData("contactus");

        if(contactUsFormData.FirstName !=='' && contactUsFormData.LastName !=='' && contactUsFormData.Email !=='' && contactUsFormData.Company == '' && contactUsFormData.Query == '' && contactUsFormData.JobRole == '') {
            MktoForms2.loadForm("//733-JCL-696.mktoweb.com", "733-JCL-696", 1025, function(form) {
                form.addHiddenFields(contactUsFormData);
                form.submit();

                form.onSuccess(function(vals,thanksURL){
                    $('.contactus-success-container', parent.document).removeClass('d-none');
                        //analytics
                        parent.window.digitalData.event = 'formSubmission';
                        parent.window.digitalData.form = {};
                        parent.window.digitalData.form.formName = 'Contact Us';
                    return false;
                });
            });
        }
    });
// contact us close

	$( ".quote-submit-btn button" ).on( "click", function() {

        $('.signup-fail-container').addClass('d-none');
		$('.signup-fail-container').text('');
        $('.signup-success-container').addClass('d-none');
        $('.signup-success-container').text('');

        $("#guideContainerForm input").change(function() {
            $('.form-success-container').addClass('d-none');
        }); $('.request-quote-btn').click(function(event) {
            $('.form-success-container').addClass('d-none');
        });

        var quote = collectFormData("quote");
        delete quote.submit;

        if(quote != false) {
            callMarketoForm(quote, 1008);
        }

    });

    $( ".request-detailed-specs-submit-btn button" ).on( "click", function() {

        $('.signup-fail-container').addClass('d-none');
    	$('.signup-fail-container').text('');
        $('.signup-success-container').addClass('d-none');
        $('.signup-success-container').text('');

        $("#guideContainerForm input").change(function() {
            $('.form-success-container').addClass('d-none');
        }); $('.request-quote-btn').click(function(event) {
            $('.form-success-container').addClass('d-none');
        });

        var formID = 0;
        if($('.form-data-container', parent.document).attr("id") !== 'undefined') {
            formID = $('.form-data-container', parent.document).attr("id");
        }
        var quote = collectFormData("quote");
        delete quote.submit;

        if(quote.FirstName !=='' && quote.LastName !=='' && quote.Email !=='' && quote.TermsAndConditions === '0') {
            callMarketoForm(quote, formID);
            if($('.form-data-container', parent.document).attr("data-path") !== 'undefined') {
                window.location.href = $('.form-data-container', parent.document).attr("data-path");
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

            loginData[name] = value;
        });
        return loginData;
    }

    function callMarketoForm (loginData, formID) {
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

        console.log(loginData);

        MktoForms2.loadForm("//733-JCL-696.mktoweb.com", "733-JCL-696", formID, function(form) {
			form.addHiddenFields(loginData);
            form.submit();

            form.onSuccess(function(vals,thanksURL){
                $('.form-success-container', parent.document).removeClass('d-none');
                $('.form-success-container', parent.document).text('Request Submitted Successfully !!!');

                //analytics
                parent.window.digitalData.event = 'formSubmission';
                parent.window.digitalData.form = {};
                if(formID === '1008') {
                    parent.window.digitalData.form.formName = 'Request Quote';
                } else if(formID === '1024') {
                    parent.window.digitalData.form.formName = 'Request Detailed Specs';
                }
                return false;
            });
        });
    }
});