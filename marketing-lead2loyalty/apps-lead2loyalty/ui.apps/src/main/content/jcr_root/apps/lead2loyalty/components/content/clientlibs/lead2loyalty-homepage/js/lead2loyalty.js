$( document ).ready(function() {
    var counter = 0;
    $('#search-button1').click(function() {
       searchFunc(0);
    });

    /*var input = document.getElementById("search");
    input.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            searchFunc();
        }
    });*/

    $(function() {
        $('.pagination').on('click', 'li', function(){
            var offset = $('a', this).attr('offset');
			searchFunc(Number(offset));
        });
	});

    function searchFunc(offset) {
        let data = document.getElementById("search").value;
        var payload = {
        	query: data
    	};

        if(payload.query !== "") {
            if(counter > 0) {
                var handles = document.querySelectorAll("#filter-counts, #searchSection, #pagination");
				for(var i = 0; i < handles.length; ++i) {
		           handles[i].innerHTML = '';
                }
                $.ajax({
                    type: "POST",
                    url: "/bin/search.json",
                    contentType: "application/json",
                    data: JSON.stringify(payload),
                    success: function(resultData) {

                        var result = JSON.parse(resultData);
                        var resultList = JSON.parse(resultData).searchResultBeanList;
                        var filterList = JSON.parse(resultData).searchFilterBeanList;
                        $("#resultCount").text("");
                        $("#resultCount").append("<b>" + result.searchResultCount + " results found for " +'"'+ payload.query +'"' + "</b>");

                        //Pagination
                        if(result.pageCount >= 1) {
                            for(var i=1; i<=result.pageCount; i++) {
                                $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+(i-1)*10+' href="#">'+i+'</a></li>');
                            }
                        }
                        //Search Filters
                        for(var i=0; i<filterList.length; i++) {
                            let newdiv = document.createElement('li');
                            newdiv.innerHTML = '<a href="#">'+filterList[i].filterTitle+' <span class="badge bg-primary">'+filterList[i].filterCount+'</span></a>';
                            document.getElementById('filter-counts').appendChild(newdiv);
                        }
                        //Render search result section
                        var limit = resultList.length < 10 ? resultList.length : (offset + 10);
                        for(var i=offset; i<limit; i++) {
                            let newDiv = document.createElement('section');
                            newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDate+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                            document.getElementById('searchSection').appendChild(newDiv);
                        }
                    }
                });
            }
            else {
                $.ajax({
                type: "POST",
                url: "/bin/search.json",
                contentType: "application/json",
                data: JSON.stringify(payload),
                success: function(resultData) {

                    var result = JSON.parse(resultData);
                    var resultList = JSON.parse(resultData).searchResultBeanList;
                    var filterList = JSON.parse(resultData).searchFilterBeanList;
                    console.log(filterList);
                    $("#resultCount").text("");
                    $("#resultCount").append("<b>" + result.searchResultCount + " results found for " +'"'+ payload.query +'"' + "</b>");

                    //Pagination
                    if(result.pageCount >= 1) {
                        for(var i=1; i<=result.pageCount; i++) {
                            $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+(i-1)*10+' href="#">'+i+'</a></li>');
                        }
                    }
                    //Search Filters
                    for(var i=0; i<filterList.length; i++) {
                        let newdiv = document.createElement('li');
                        newdiv.innerHTML = '<a class="badge-class" href="#">'+filterList[i].filterTitle+' <span class="badge bg-primary">'+filterList[i].filterCount+'</span></a>';
                        document.getElementById('filter-counts').appendChild(newdiv);
                    }
                    //Render search result section
					var limit = resultList.length < 10 ? resultList.length : (offset + 10);
                    for(var i=offset; i<limit; i++) {
                        let newDiv = document.createElement('section');
                        newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDate+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                        document.getElementById('searchSection').appendChild(newDiv);
                    }
            	}
        	});
        }
		 counter = counter + 1;
        }
        else {
            var handles = document.querySelectorAll("#filter-counts, #searchSection, #pagination, #resultCount");
            for(var i = 0; i < handles.length; ++i) {
		        handles[i].innerHTML = '';
            }
			$("#resultCount").append("<b> No Search keyword found!!! </b>");
        }
    }
});