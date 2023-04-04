$( document ).ready(function() {

    if(localStorage.getItem('userDetails')) {
        var user = JSON.parse(localStorage.getItem('userDetails'));
        var userEmail = user.email;
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
         else {
            alert("Please enter a value");
         }
    });

    function initialSearch() {
        let data = localStorage.getItem("searchTerm");
        document.getElementById("search").value = data;
        document.getElementById("search-button1").click();
    }

    var category1 = []; //Global variable for First Category
    var category2 = []; //Global variable for Second Category
    var category3 = []; //Global variable for Third Category
    var category4 = []; //Global variable for Fourth Category

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
                            let newdiv = document.createElement('div');
                            let divIdName = 'FilterButton'+i;
                        	newdiv.setAttribute("id", divIdName);
                            newdiv.innerHTML = '<button type="button" class="btn d-flex expanded inbtn">'+filterList[i].filterTitle+' <span class="badge bg-primary">'+filterList[i].filterCount+'</span></button>';
                            document.getElementById('filter-counts').appendChild(newdiv);
                        }
                        $('#FilterButton0').click(function() {
                            CategoryFilter(this.innerHTML);
                        });
                        $('#FilterButton1').click(function() {
                            CategoryFilter(this.innerHTML);
                        });
                        $('#FilterButton2').click(function() {
                            CategoryFilter(this.innerHTML);
                        });
                        $('#FilterButton3').click(function() {
                            CategoryFilter(this.innerHTML);
                        });

                         //Segregate Search Results
                        for(var i=offset; i<resultList.length; i++) {
                            if(resultList[i].category == "Product Category Page"){category1.push((resultList[i]));}
                            if(resultList[i].category == "Article Page"){category2.push((resultList[i]));}
                            if(resultList[i].category == "Product Detail page"){category3.push((resultList[i]));}
                            if(resultList[i].category == "Home Page"){category4.push((resultList[i]));}
                        }

                        //Render search result section
                        var limit = resultList.length < 10 ? resultList.length : (offset + 10);
                        for(var i=offset; i<limit; i++) {
                            let newDiv = document.createElement('section');
                            if( resultList[i].imagePath == '') {
                                resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';
                            }
                            newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-12"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDate+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                            document.getElementById('searchSection').appendChild(newDiv);
                        }
                        //Search Analytics
                        digitalData.event = 'siteSearch';
                        digitalData.siteSearch = {
                            'term':payload.query,
                            'results': result.searchResultCount,
                            'filter': ''
                        };
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
                        let newdiv = document.createElement('div');
                        let divIdName = 'FilterButton'+i;
                        newdiv.setAttribute("id", divIdName);
                        newdiv.innerHTML = '<button type="button" class="btn d-flex expanded">'+filterList[i].filterTitle+' <span class="badge bg-primary">'+filterList[i].filterCount+'</span></button>';
                        document.getElementById('filter-counts').appendChild(newdiv);
                    }

                    $('#FilterButton0').click(function() {
                        CategoryFilter(this.innerHTML);
                    });
                    $('#FilterButton1').click(function() {
                        CategoryFilter(this.innerHTML);
                    });
                    $('#FilterButton2').click(function() {
                        CategoryFilter(this.innerHTML);
                    });
                    $('#FilterButton3').click(function() {
                        CategoryFilter(this.innerHTML);
                    });

                    //Segregate Search Results
                    for(var i=offset; i<resultList.length; i++){
                        if(resultList[i].category == "Product Category Page"){category1.push((resultList[i]));}
                        if(resultList[i].category == "Article Page"){category2.push((resultList[i]));}
                        if(resultList[i].category == "Product Detail page"){category3.push((resultList[i]));}
                        if(resultList[i].category == "Home Page"){category4.push((resultList[i]));}
                    }
                    //Render search result section
					var limit = resultList.length < 10 ? resultList.length : (offset + 10);
                    for(var i=offset; i<limit; i++) {
                        let newDiv = document.createElement('section');
                        if( resultList[i].imagePath == '') {
                            resultList[i].imagePath = '/content/dam/lead2loyalty/no-img.jpg';
                        }
                        newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+resultList[i].pagePath+'.html" target="_blank"><img class="image" src="'+resultList[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-12"><h5 class="search-result-item-heading"><a href="#"> '+resultList[i].title+' </a></h5><p class="info">'+resultList[i].publishDate+'</p><p class="description">'+resultList[i].description+'</p><div class="custom-buttons"><span class="badge ">'+resultList[i].keywords+'</span></div></div></div></div></section>';
                        document.getElementById('searchSection').appendChild(newDiv);
                    }
                    //Search Analytics
                    digitalData.event = 'siteSearch';
                    digitalData.siteSearch = {
                        'term':payload.query,
                        'results': result.searchResultCount,
                        'filter': ''
                    };
            	}
        	});
        }
	    counter = counter + 1;
		category1.splice(0, category1.length);
        category2.splice(0, category2.length);
        category3.splice(0, category3.length);
        category4.splice(0, category3.length);
        }
        else {
            var handles = document.querySelectorAll("#filter-counts, #searchSection, #pagination, #resultCount");
            for(var i = 0; i < handles.length; ++i) {
		        handles[i].innerHTML = '';
            }
			$("#resultCount").append("<b> No Search keyword found!!! </b>");
        }
    }

    function CategoryFilter(Data) {
        let offset = 0;
        let FilterData = Data;
        let numbers = FilterData.match(/\d+/g);
        let FCount = parseInt(numbers[0],10);
        var handles = document.querySelectorAll("#searchSection, #pagination");
        for(var i = 0; i < handles.length; ++i) {
          handles[i].innerHTML = '';
        }

        //Pagination
        let x = Math.ceil(FCount/10);
        if(x >= 1) {
            for(var i=1; i<= x; i++) {
                $("#pagination").append('<li class="page-item"><a class="page-link" offSet='+(i-1)*10+' href="#">'+i+'</a></li>');
            }
        }

        //Render search result section
        // var limit = resultList.length < 10 ? resultList.length : (offset + 10);
        for(var i=offset; i<FCount; i++) {
            if(FilterData.includes("Product Category Page ")) {
                let newDiv = document.createElement('section');
                newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+category1[i].pagePath+'.html" target="_blank"><img class="image" src="'+category1[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+category1[i].title+' </a></h5><p class="info">'+category1[i].publishDate+'</p><p class="description">'+category1[i].description+'</p><div class="custom-buttons"><span class="badge ">'+category1[i].keywords+'</span></div></div></div></div></section>';
                document.getElementById('searchSection').appendChild(newDiv);
            }
            if(FilterData.includes("Article Page")) {
                let newDiv = document.createElement('section');
                newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+category2[i].pagePath+'.html" target="_blank"><img class="image" src="'+category2[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+category2[i].title+' </a></h5><p class="info">'+category2[i].publishDate+'</p><p class="description">'+category2[i].description+'</p><div class="custom-buttons"><span class="badge ">'+category2[i].keywords+'</span></div></div></div></div></section>';
                document.getElementById('searchSection').appendChild(newDiv);
            }
            if(FilterData.includes("Product Detail page")) {
                let newDiv = document.createElement('section');
                newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+category3[i].pagePath+'.html" target="_blank"><img class="image" src="'+category3[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+category3[i].title+' </a></h5><p class="info">'+category3[i].publishDate+'</p><p class="description">'+category3[i].description+'</p><div class="custom-buttons"><span class="badge ">'+category3[i].keywords+'</span></div></div></div></div></section>';
                document.getElementById('searchSection').appendChild(newDiv);
            }
            if(FilterData.includes("Home Page")) {
                let newDiv = document.createElement('section');
                newDiv.innerHTML = '<div class="search-result-item"> <a class="image-link" href="'+category4[i].pagePath+'.html" target="_blank"><img class="image" src="'+category4[i].imagePath+'"></a><div class="search-result-item-body"><div class="row"><div class="col-sm-9"><h5 class="search-result-item-heading"><a href="#"> '+category4[i].title+' </a></h5><p class="info">'+category4[i].publishDate+'</p><p class="description">'+category4[i].description+'</p><div class="custom-buttons"><span class="badge ">'+category4[i].keywords+'</span></div></div></div></div></section>';
                document.getElementById('searchSection').appendChild(newDiv);
            }

        }

    }

});