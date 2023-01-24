if(localStorage.getItem('userDetails')) {
	$('.user-logged-in').removeClass('d-none');
    $('.user-log-in').addClass('d-none');
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
    var filterName = $('.filter-header[data-name="'+filterCode+'"]').attr('data-title');
    $('.resultsWrapper[data-parent="'+filterName+'"]').removeClass('d-none');

    $('.filter-header').click(function(event) {
        $('.resultsWrapper').addClass('d-none');
        var filterTitle = $(event.currentTarget).attr('data-title');
        $('.resultsWrapper[data-parent="'+filterTitle+'"]').removeClass('d-none');
    });

    $('.clear-all-btn').click(function(event) {
        $('.resultsWrapper').addClass('d-none');
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
        '<div class="col-4"><label for="exampleInputTeam'+(variableIndex+1)+'" class="form-label">Team</label><input type="text" class="form-control" id="exampleInputTeam'+(variableIndex+1)+'"></div>'+
        '<div class="col-4"><label for="exampleInputName'+(variableIndex+1)+'" class="form-label">Name</label><input type="text" class="form-control" id="exampleInputName'+(variableIndex+1)+'"></div>'+
        '<div class="col-4"><label for="exampleInputEmail'+(variableIndex+1)+'" class="form-label">Email ID</label><input type="email" class="form-control" id="exampleInputEmail'+(variableIndex+1)+'"></div></div>';
        $('.share-team-form .table-container').append(additionalRow);
        variableIndex++;
    });
}


$('.login-btn').click(function(event) {

    var loginData = {
        "username" : "test@gmail.com",
        "password" : "test"
    };

	$.ajax({
      type: "POST",
      url: "/bin/user",
      data: JSON.stringify(loginData),
      contentType: "application/json",
      dataType: "json",
      success: function(resultData){
          alert("User Logged in Successfully !!!");

          if(this.checkValidity()) {
              event.preventDefault();
            var formValues = JSON.stringify($(this).serializeArray());
            localStorage.setItem('userDetails', formValues);
              $('.user-logged-in').removeClass('d-none');
              $('.user-log-in').addClass('d-none');
            $('#signInFormModal').modal('hide');
            $('.modal-backdrop').remove();
          }
      }
	});
});


$('.logout-app').click(function(event) {
	localStorage.setItem('userDetails', '');
    $('.user-log-in').removeClass('d-none');
    $('.user-logged-in').addClass('d-none');
});