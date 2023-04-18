$( document ).ready(function() {

    //Search Suggestions
    var list = [];
    const dataID = document.getElementById('search');
    if(dataID) {
        const suggestions = JSON.parse(dataID.getAttribute('data-suggestions'));
    	suggestions.forEach(function(suggestion) {
    		list.push(suggestion);
    	});
    }

    $("#search").autocomplete({
    	source: list,
    	minLength: 3,
    	select: function(click,ui) {
    		searchFunc(ui.item.value, 0);
        }
    });

	// redirection to current page post registration & login
    $("#register-cta").click(function () {
        var separator = $(this).attr('href').includes('?') ? '&' : '?';
        var currentPagePath = $(this).attr('href') + separator + 'page=' + $(location).attr("pathname").replace(/\.[^/.]+$/, "");
        $(this).attr("href", currentPagePath);
    });

    //retrieve browser cookie
    function getCookie(name) {
        function escape(s) { return s.replace(/([.*+?\^$(){}|\[\]\/\\])/g, '\\$1'); }
        var match = document.cookie.match(RegExp('(?:^|;\\s*)' + escape(name) + '=([^;]*)'));
        return match ? match[1] : null;
    }

    // bookmark product & Articles
    $("#bookmarkBtn").click(function () {
        var user = JSON.parse(getCookie('userDetails'));
        if(user !== null) {
            var pagePath = $(location).attr("pathname").replace(/\.[^/.]+$/, "");
            var className = $('#bookmark1').attr("class");
            var action = className === 'fa fa-bookmark-o' ? 'add' : 'remove';
            var payload = {
                action: action,
                email: user.email,
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

	// bookmark filter toggle
	$( ".article-filter" ).on( "click", function() {
        $('.Product_Detail_Page').addClass('d-none');
        $('.Article_Page').removeClass('d-none');
        $('.article-filter').addClass('highlight');
        $('.product-filter').removeClass('highlight');
    });

    $( ".product-filter" ).on( "click", function() {
        $('.Article_Page').addClass('d-none');
    	$('.Product_Detail_Page').removeClass('d-none');
    	$('.product-filter').addClass('highlight');
    	$('.article-filter').removeClass('highlight');
    });

    //bookmark clear all
    $( ".ClearAllBtn" ).on( "click", function() {
        $('.Article_Page,.Product_Detail_Page').removeClass('d-none')
    	$('.product-filter,.article-filter').removeClass('highlight');
    });

    //ClearAll Search Results
         $( "#clearAll" ).on( "click", function() {
             searchFunc(document.getElementById("search").value, 0);
        });

    //Search Sort By
    $('#search-sort-by').change(function() {
        searchFunc(document.getElementById("search").value, 0);
    });

    //Search Result Section
	var globalSearchTerm;
    if (window.location.href.includes("search-result.html")) {
         $('#form1').attr('disabled', 'disabled');
        $('#search-button').attr('disabled', 'disabled');
        setTimeout(initialSearch,1000);
    }

    $('#search-button').click(function() {
         globalSearchTerm = document.getElementById("form1").value;
         if(globalSearchTerm !=='') {
             localStorage.setItem("searchTerm", globalSearchTerm);
             window.location.href = "/content/lead2loyalty/language-masters/en/search-result.html?search=#";
             initialSearch();
         }
         else {
            alert("Please enter a value");
         }
    });

   //Global Search Enter button
   var globalInput = document.getElementById("form1");
   globalInput.addEventListener("keypress", function(event) {
        if (event.key === "Enter") {
            event.preventDefault();
            globalSearchTerm = document.getElementById("form1").value;
            if(globalSearchTerm !=='') {
                localStorage.setItem("searchTerm", globalSearchTerm);
                window.location.href = "/content/lead2loyalty/language-masters/en/search-result.html?search=#";
                initialSearch();
            }
            else{
                alert("Please enter a value");
            }
        }
    });
    //End Global Search

   //initial Search
   function initialSearch() {
       let data = localStorage.getItem("searchTerm");
       document.getElementById("search").value = data;
       document.getElementById("search-button1").click();
   }
    var filterObj = [];
    var resultObj = [];

    var counter = 0;
    $('#search-button1').click(function() {
       searchFunc(document.getElementById("search").value, 0);
    });

    //Search Page Enter button
    if (window.location.href.includes("search-result.html")) {
        var input = document.getElementById("search");
        input.addEventListener("keypress", function(event) {
            if (event.key === "Enter") {
                event.preventDefault();
                searchFunc(document.getElementById("search").value, 0);
            }
        });
    }
    //End Initial Search

    //Pagination
    $(function() {
        $('.pagination').on('click', 'li', function(){
            var pagetag = $('a', this).attr('pagetag');

            if(Number(pagetag) == 1){
            var offset = $('a', this).attr('offset');
                searchFunc(document.getElementById("search").value, Number(offset));
            }
            else if(Number(pagetag) == 0) {
                var offset = $('a', this).attr('offset');
                var categoryTag = $('.Highlight').attr('categoryTag');
                let categoryHighlight = document.getElementById("HighlightBtn");
                CategoryFilter(categoryHighlight,Number(categoryTag),Number(offset));
            }
        });
	});
    //End Pagination

    //Search Fun Start
    function searchFunc(searchKeyword, offset) {
        filterObj.splice(0, filterObj.length);
        resultObj.splice(0, resultObj.length);
        let sortBy = $('#search-sort-by').find(":selected").text();
        var payload = {
            query: searchKeyword,
            sortBy: sortBy
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
                        if(resultData && JSON.parse(resultData).status) {
                            var result = JSON.parse(resultData);
                            var resultList = JSON.parse(resultData).searchResultBeanList;
                            var filterList = JSON.parse(resultData).searchFilterBeanList;
                            $("#resultCount").text("");
                            $("#resultCount").append("<b>" + result.searchResultCount + " results found for " +'"'+ payload.query +'"' + "</b>");

                            //Pagination
                            if(result.pageCount >= 1) {
                                for(var i=1; i<=result.pageCount; i++) {
                                    $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+(i-1)*10+' pagetag='+1+' href="#">'+i+'</a></li>');
                                }
                            }

                            //Search Filters
                            for(var i=0; i<filterList.length; i++) {
                                filterObj.push((filterList[i]));
                                let newdiv = document.createElement('div');
                                newdiv.innerHTML = '<div class="FilterDiv" categoryTag='+i+'><button type="button" class="btn expanded custom-category-btn">'+filterList[i].filterTitle+'</button><span class="badge bg-primary custom-badge">'+filterList[i].filterCount+'</span></div>';
                                document.getElementById('filter-counts').appendChild(newdiv);
                            }
                            $('.FilterDiv').click(function() {
                                var categoryTag = $(this).attr('categoryTag');
                                CategoryFilter(this,Number(categoryTag),0);
                            });

                            //Render search result section
                            var limit = resultList.length < 10 ? resultList.length : (offset + 10);
                            if(limit>resultList.length){limit = resultList.length;}
                            for(var i=offset; i<limit; i++) {
                                let newDiv = document.createElement('section');
                                if( resultList[i].imagePath == '') {
                                    resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';
                                }
                                newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-12"><h5 class="search-result-item-heading"><a href="'+resultList[i].pagePath+'.html"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDateAsString+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                                document.getElementById('searchSection').appendChild(newDiv);
                            }

                            for(var i=0; i<resultList.length; i++) {
                                if(resultList[i].imagePath == '') {
                                   resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';
                                }
                                resultObj.push((resultList[i]));
                            }
                            //Search Analytics
                            digitalData.event = 'siteSearch';
                            digitalData.siteSearch = {
                                'term':payload.query,
                                'results': result.searchResultCount,
                                'filter': ''
                            };
                        }
                        else {
                            $("#resultCount").text("");
                            $("#resultCount").append("<b>" + " Something went wrong. " + "</b>");
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
                        if(resultData && JSON.parse(resultData).status) {
                            var result = JSON.parse(resultData);
                            var resultList = JSON.parse(resultData).searchResultBeanList;
                            var filterList = JSON.parse(resultData).searchFilterBeanList;
                            $("#resultCount").text("");
                            $("#resultCount").append("<b>" + result.searchResultCount + " results found for " +'"'+ payload.query +'"' + "</b>");

                            //Pagination
                            if(result.pageCount >= 1) {
                                for(var i=1; i<=result.pageCount; i++) {
                                    $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+(i-1)*10+' pagetag='+1+' href="#">'+i+'</a></li>');
                                }
                            }

                            //Search Filters
                            for(var i=0; i<filterList.length; i++) {
                                filterObj.push((filterList[i]));
                                let newdiv = document.createElement('div');
                                newdiv.innerHTML = '<div class="FilterDiv" categoryTag='+i+'><button type="button" class="btn expanded custom-category-btn">'+filterList[i].filterTitle+'</button> <span class="badge bg-primary custom-badge">'+filterList[i].filterCount+'</span><div>';
                                document.getElementById('filter-counts').appendChild(newdiv);
                            }
                            $('.FilterDiv').click(function() {
                                var categoryTag = $(this).attr('categoryTag');
                                CategoryFilter(this,Number(categoryTag),0);
                            });

                            //Render search result section
                            var limit = resultList.length < 10 ? resultList.length : (offset + 10);
                            if(limit>resultList.length){ limit = resultList.length; }
                            for(var i=offset; i<limit; i++) {
                                let newDiv = document.createElement('section');
                                if( resultList[i].imagePath == '') {
                                    resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';
                                }
                                newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-12"><h5 class="search-result-item-heading"><a href="'+resultList[i].pagePath+'.html"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDateAsString+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                                document.getElementById('searchSection').appendChild(newDiv);
                            }
                            for(var i=0; i<resultList.length; i++) {
                               if( resultList[i].imagePath == '') {
                                   resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';
                               }
                               resultObj.push((resultList[i]));
                            }
                            //Search Analytics
                            digitalData.event = 'siteSearch';
                            digitalData.siteSearch = {
                                'term':payload.query,
                                'results': result.searchResultCount,
                                'filter': ''
                            };
                        }
                        else {
                            $("#resultCount").text("");
                            $("#resultCount").append("<b>" + " Something went wrong. " + "</b>");
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
    //Search fun Ending

    //Category Filter fun
    function CategoryFilter(obj,Tag,offset) {

        $(".Highlight").removeClass("Highlight");
        $(".Highlight").removeAttr('id');
   		$(obj).addClass("Highlight");
        $(obj).attr('id', 'HighlightBtn');
        let localOffset = offset ;
        var handles = document.querySelectorAll("#searchSection, #pagination");
    	for(var i = 0; i < handles.length; ++i) {
    	    handles[i].innerHTML = '';
        }
    	//Pagination
        let x = Math.ceil(parseInt(filterObj[Tag].filterCount)/10);
        if(x >= 1) {
            for(var i=1; i<= x; i++) {
                $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+(i-1)*10+' pageTag='+0+' href="#">'+i+'</a></li>');
            }
        }
        //Render search result section
        var limit = resultObj.length < 10 ? resultObj.length : (localOffset + 10);
        if(limit>resultObj.length){ limit = resultObj.length; }

        var temporaryResults = [];
        for(var i=0;i<resultObj.length;i++) {
            if(filterObj[Tag].filterTitle == resultObj[i].category) {
                temporaryResults.push((resultObj[i]));
            }
        }
        for(var i=localOffset;i<limit;i++) {
            let newDiv = document.createElement('section');
            newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+temporaryResults[i].pagePath+'.html" target="_blank"><img class="image" src="'+temporaryResults[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="'+temporaryResults[i].pagePath+'.html"> '+temporaryResults[i].title+' </a></h5><p class="info">'+temporaryResults[i].publishDateAsString+'</p><p class="description">'+temporaryResults[i].description+'</p><div class="custom-buttons"><span class="badge ">'+temporaryResults[i].keywords+'</span></div></div></div></div></section>';
            document.getElementById('searchSection').appendChild(newDiv);
        }
    }
    //End Category filter fun
});