if(localStorage.getItem('userDetails')) {
	$('.user-logged-in').removeClass('d-none');
    $('.user-log-in').addClass('d-none');
}



if (digitalData && digitalData.user && digitalData.user.profile && digitalData.user.profile.email) {
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
        filterCode = $('.filter-header').eq(0).attr('data-name');
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

if($('.product-details').length) {
    var variableIndex;
	$(document).on('show.bs.modal', '.modal', function () {
        $(".modal-backdrop").not(':first').remove();
        $('.share-team-form')[0].reset();
        $('.table-container').empty();
        variableIndex = 2;
    });

    $('.add-team-details').click(function(event) {
        var additionalRow = '<div class="row mb-4">'+
        '<div class="col-6"><label for="exampleInputName'+(variableIndex+1)+'" class="form-label">First Name</label><input type="text" class="form-control" id="exampleInputName'+(variableIndex+1)+'"></div>'+
        '<div class="col-6"><label for="exampleInputEmail'+(variableIndex+1)+'" class="form-label">Email</label><input type="email" class="form-control" id="exampleInputEmail'+(variableIndex+1)+'"></div></div>';
        $('.share-team-form .table-container').append(additionalRow);
        variableIndex++;
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