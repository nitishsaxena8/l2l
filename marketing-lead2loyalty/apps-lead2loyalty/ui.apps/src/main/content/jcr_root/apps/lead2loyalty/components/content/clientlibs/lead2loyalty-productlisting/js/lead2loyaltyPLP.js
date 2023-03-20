if(localStorage.getItem('userDetails')) {
	$('.user-logged-in').removeClass('d-none');
    $('.user-log-in').addClass('d-none');
}



if (digitalData && digitalData.user && digitalData.user.email) {
   $("#requestAQuoteMember").removeClass("d-none");
   $("#requestAQuoteGuest").addClass("d-none");
} else {
	$("#requestAQuoteMember").addClass("d-none");
    $("#requestAQuoteGuest").removeClass("d-none");
}

if($('.plp-container').length) {
    var url = document.location.href;
	var queryURL = url.substring(url.indexOf('&') + 1);
    var filterCode;
    $('.resultsWrapper').addClass('d-none');
    $('#accordion .collapse').removeClass('show');
	if(queryURL.split('category').length === 2) {
        filterCode = queryURL.split('=')[1];
    } else {
        //filterCode = $('.filter-header').eq(0).attr('data-name');
        $('.resultsWrapper').removeClass('d-none');
        $('.filter-header').removeClass('expanded');
        $('#accordion .collapse').removeClass('show');
    }
    $('.filter-header[data-name="'+filterCode+'"]').trigger('click');
    $('.filter-header[data-name="'+filterCode+'"]').addClass('expanded');
    var filterName = $('.filter-header[data-name="'+filterCode+'"]').attr('data-title');
    $('.resultsWrapper[data-parent="'+filterName+'"]').removeClass('d-none');

    $('.filter-header').click(function(event) {
        $('.resultsWrapper').addClass('d-none');
        var filterTitle = $(event.currentTarget).attr('data-title');
        $('.resultsWrapper[data-parent="'+filterTitle+'"]').removeClass('d-none');
        $('#accordion .collapse').removeClass('show');
		$('.filter-header').removeClass('expanded');
        var isCollapsed = $(event.currentTarget).parent('.card-header').siblings('.collapse').hasClass('show');
        if(isCollapsed) {
			$(event.currentTarget).removeClass('expanded');
        } else {
            $(event.currentTarget).addClass('expanded');
        }
    });

    $('.clear-all-btn').click(function(event) {
        $('.resultsWrapper').removeClass('d-none');
        $('.filter-header').removeClass('expanded');
        $('#accordion .collapse').removeClass('show');
    });
}

if($('#shareWithTeamsBtnModal').length) {
    var variableIndex;
	$(document).on('show.bs.modal', '.modal', function () {
        $(".modal-backdrop").not(':first').remove();
        $('.share-team-form')[0].reset();
        $('.table-container').empty();
    });

    // $('.add-team-details').click(function(event) {
    //     var additionalRow = '<div class="row mb-4 team-line-item">'+
    //     '<div class="col-6"><label for="inputTeamName'+(variableIndex+1)+'" class="form-label">First Name</label><input type="text" class="form-control" name="firstnameTeam'+(variableIndex+1)+'" id="inputTeamName'+(variableIndex+1)+'"></div>'+
    //     '<div class="col-6"><label for="inputTeamEmail'+(variableIndex+1)+'" class="form-label">Email</label><input type="email" class="form-control" name="emailTeam'+(variableIndex+1)+'" id="inputTeamEmail'+(variableIndex+1)+'"></div></div>';
    //     $('.share-team-form .table-container').append(additionalRow);
    //     variableIndex++;
    //     $('.form-success-container.share-with-team').addClass('d-none');
    // });

    $('#shareWithTeamsBtn').click(function() {        
        variableIndex = 2;
        $('.form-success-container.share-with-team').addClass('d-none');
    });

    $(".share-team-form input").change(function() { 
        $('.form-success-container.share-with-team').addClass('d-none');
    });

    $('#shareWithTeamForm').submit(function(event) {
        $('.form-success-container.share-with-team').addClass('d-none');
        var formID = $('.form-success-container').attr('form-id');
        if(this.checkValidity()) {
            event.preventDefault();
			var requestData = [];
            var transferSingleObj = {};
            var pagePath= '';
            if($('.share-team-form input').length) {
                $.each($('.share-team-form .team-line-item'), function(index, obj) {
					var dataObj = {};
                    var firstNameVal = $(obj).find('input').eq(0).val();
                    var emailVal = $(obj).find('input').eq(1).val();

                    if(firstNameVal && emailVal) {
                        dataObj.FirstName = firstNameVal;
                        dataObj.Email = emailVal;
                        dataObj.productId = $('.product-page-details').attr('product-id');
                        dataObj.productTitle = $('.product-page-details').attr('product-title');
                        dataObj.productDescription = $('.product-page-details').attr('product-description');
                        dataObj.productPath = $('.product-page-details').attr('product-path');
                        pagePath = dataObj.productPath;
                        //requestData.push(JSON.stringify(dataObj));

                        if(index === 0) {
                            transferSingleObj = dataObj;
                        }
                    }
                });
            }

            MktoForms2.loadForm("//733-JCL-696.mktoweb.com", "733-JCL-696", formID, function(form) {
                form.addHiddenFields(transferSingleObj);
                form.submit();
                console.log("Form Submitted !!!")
                console.log(transferSingleObj);
                form.onSuccess(function(vals,thanksURL) {
                    $(".form-success-container.share-with-team", parent.document).removeClass('d-none');
                    digitalData.event = 'formSubmission';
                	digitalData.form = {};
                	digitalData.form.formName = pagePath.includes("/products") ? 'Share the Product' : 'Share the Article';
                    return false;
                });
            });
        }
    });
}


$('#signInForm').submit(function(event) {
    $('.sign-in-error').addClass('d-none');
    if(this.checkValidity()) {
        event.preventDefault();
        var loginData = {
            "username" : $('#loginEmail').val(),
            "password" : $('#loginPassword').val()
        };

        $.ajax({
          type: "POST",
          url: "/bin/user",
          data: JSON.stringify(loginData),
          contentType: "application/json",
          dataType: "json",
          success: function(resultData) {
              if (resultData.errorCode) {
                $('.sign-in-error').removeClass('d-none');
                $('.sign-in-error').text(resultData.errorMessage);
              } else {
                console.log(resultData);
                localStorage.setItem('userDetails', JSON.stringify(resultData));
                $('.user-logged-in').removeClass('d-none');
                $('.user-log-in').addClass('d-none');
                $('[data-target="#signInFormModal"]').trigger('click');
                $('.modal-backdrop').remove();

                //analytics
                digitalData.event = 'loggedIn';
                digitalData.user = digitalData.user || {};
                digitalData.user.authState = 'authenticated';
                digitalData.user.userType = 'member';
                digitalData.user.email = resultData.email;

                if($("#requestAQuoteMember")) {
   					$("#requestAQuoteMember").removeClass("d-none");
   				$("#requestAQuoteGuest").addClass("d-none");
                  }

              }
          },
          error: function(errorData) {
			  $('.sign-in-error').removeClass('d-none');
          }
        });
    }
});

$('.logout-app').click(function(event) {
	localStorage.setItem('userDetails', '');
    $('.user-log-in').removeClass('d-none');
    $('.user-logged-in').addClass('d-none');
    $('.user-logged-in  .dropdown-menu').removeClass('show');

    //analytics
    digitalData.event = '';
    digitalData.user = digitalData.user || {};
    digitalData.user.authState = 'not-authenticated';
    digitalData.user.userType = 'guest';
    digitalData.user.email = '';
});

$('.user-logged-in .dropdown-toggle').click(function(event) {
    $('.user-logged-in .dropdown-menu').toggleClass('show');
});

$("#signInFormModal .form-control").change(function() {
    $('.sign-in-error').addClass('d-none');
});

$('[data-bs-toggle="modal"]').click(function(event) {
    var modalTarget = $(this).attr('data-target');    
    if($(modalTarget).find('form').length) {
        $(modalTarget).find('form')[0].reset(); 
    }
});

//Article Download
$("body").on("click", "#btnExport", function () {
    setTimeout(function() {
        html2canvas($('#tblCustomers')[0],{
            onrendered: function (canvas) {
                var data = canvas.toDataURL();
                var docDefinition = { content: [{image: data,width: 500 }]};
                //pdfMake.createPdf(docDefinition).download("data.pdf");
                pdfMake.createPdf(docDefinition).download($('.product-page-details', parent.document).attr("product-title")+".pdf");
            }
        });
    },2000);
});

$( document ).ready(function() {
    $('#requestAQuoteMember').click(function(event) {
    	$("#guideContainerForm input").change(function() {
            $('.form-success-container').addClass('d-none');
        }); $('.request-quote-btn').click(function(event) {
            $('.form-success-container').addClass('d-none');
        });

        var quote = {};
        var formID = 1008;
        if($('.form-data-container').attr('id') != 'undefined'){
			formID = $('.form-data-container').attr('id');
        }

        if (localStorage.getItem('userDetails')) {
            var user = JSON.parse(localStorage.getItem('userDetails'));
            if (user) {

				quote = {
                    "firstName": user.firstName,
                    "lastName": user.lastName,
                    "email": user.email
                }
            }
        }

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

        MktoForms2.loadForm("//733-JCL-696.mktoweb.com", "733-JCL-696", formID, function(form) {
            form.addHiddenFields(quote);
            form.submit();
            console.log("Form Submitted !!!")
			console.log(quote);
            form.onSuccess(function(vals,thanksURL){
                $('.form-success-container', parent.document).removeClass('d-none');
                digitalData.event = 'formSubmission';
                digitalData.form = {};
                if(formID == 1008){
                    digitalData.form.formName = 'Request Quote';
                } else if(formID == 1024){
                    digitalData.form.formName = 'Download Technical Specifications';
                }

                return false;
            });
        });
        var downloadResourcePath = $('.form-data-container').data('path');
        if(downloadResourcePath !== 'undefined') {
            window.location.href = downloadResourcePath;
        }

        if ($('.product-page-details', parent.document) && $('.product-page-details', parent.document).attr("product-title")) {
            $('#requestAQuotemdelTitle', parent.document).text("Request a Quote - "+$('.product-page-details', parent.document).attr("product-title"));
        }
    });
});
