$( document ).ready(function() {

    if(localStorage.getItem('userDetails')) {
        var user = JSON.parse(localStorage.getItem('userDetails'));
        var userEmail = user.email;
        document.cookie = "userEmail="+userEmail;
        $("#dropdownMenuLink").text("Hi "+ user.firstName);
    }
    function getCookie(name) {
        function escape(s) { return s.replace(/([.*+?\^$(){}|\[\]\/\\])/g, '\\$1'); }
        var match = document.cookie.match(RegExp('(?:^|;\\s*)' + escape(name) + '=([^;]*)'));
        return match ? match[1] : null;
    }
    $("#bookmarkBtn").click(function () {

        if(getCookie("userEmail") !== null) {

            var pagePath = $(location).attr("pathname").replace(/\.[^/.]+$/, "");
            var className = $('#bookmark1').attr("class");
            var action = className === 'fa fa-bookmark-o' ? 'add' : 'remove';

            var payload = {
                action: action,
                email: userEmail,
                pagePath: pagePath
            };

            $.ajax({
                type: "POST",
                url: "/bin/manageBookmarks.json",
                contentType: "application/json",
                data: JSON.stringify(payload),
                success: function(resultData) {
                    $("#bookmark1").toggleClass("fa-bookmark-o fa-bookmark");
                }
            });
        }
        else {
			alert("Please sign in to bookmark");
        }

	});

	 var globalSearchTerm;

    if (window.location.href.includes("search-result.html")) {
         $('#form1').attr('disabled', 'disabled');
        $('#search-button').attr('disabled', 'disabled');
        setTimeout(initialSearch,1000);
    }

 $('#search-button').click(function() {
     globalSearchTerm = document.getElementById("form1").value;
     if(globalSearchTerm !==''){
     localStorage.setItem("searchTerm", globalSearchTerm);
     window.location.href = "/content/lead2loyalty/language-masters/en/search-result.html?search=#";

     initialSearch();

     }
     else{
         alert("Please enter a value");
     }
});

function initialSearch(){
        let data = localStorage.getItem("searchTerm");
        document.getElementById("search").value = data;
        document.getElementById("search-button1").click();
    }



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
                            if( resultList[i].imagePath == ''){
                                  resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';

                         }
                            newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-12"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDate+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
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
                        if( resultList[i].imagePath == ''){
                                       resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';

                          }
                        newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-12"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDate+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
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