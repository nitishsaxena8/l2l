if($('.plp-container').length) {
    $('.resultsWrapper[data-parent="Development Board"]').removeClass('d-none');
    $('.accordionFilter').click(function(event) {
        $('.resultsWrapper').addClass('d-none');
        var cardTitle = $(event.currentTarget).find('.cardTitle').attr('data-title');
        $('.resultsWrapper[data-parent="'+cardTitle+'"]').removeClass('d-none');
    });
    
    $('.cardTitle').click(function(event) {
       var panelContent = $(event.currentTarget).attr('data-target');
       $(panelContent).toggleClass('show');
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
