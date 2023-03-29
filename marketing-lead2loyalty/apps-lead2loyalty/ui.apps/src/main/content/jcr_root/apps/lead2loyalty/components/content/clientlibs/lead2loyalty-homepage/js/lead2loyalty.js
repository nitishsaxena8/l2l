$( document ).ready(function() {
    $("#search-button1").click(function(e) {
        let data = document.getElementById("search").value;
        var payload = {
        	query: data,
            offSet: "0"
    	};
        if(payload.query !== "") {
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

                if(result.pageCount > 2) {
                    console.log(result.pageCount);
                    for(var i=1; i<=result.pageCount; i++) {
                        $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+i*10+' href="#">'+i+'</a></li>');

                    }

                }

                for(var i=0; i<filterList.length; i++) {
					let newdiv = document.createElement('li');
  					newdiv.innerHTML = '<a href="#">'+filterList[i].filterTitle+' <span class="badge bg-primary">'+filterList[i].filterCount+'</span></a>';
  					document.getElementById('filter-counts').appendChild(newdiv);
                    }

                for(var i=0; i<resultData.length; i++) {

                    let newDiv = document.createElement('section');
                    newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">New York, NY 20188</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                    document.getElementById('searchSection').appendChild(newDiv);
        			}

            	}
        	});
        }
        else{
			$("#resultCount").append("<b> No Search keyword found!!! </b>");
        }

    });

});