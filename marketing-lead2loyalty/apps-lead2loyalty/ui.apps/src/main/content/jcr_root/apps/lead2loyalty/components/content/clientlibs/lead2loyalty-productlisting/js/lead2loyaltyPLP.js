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