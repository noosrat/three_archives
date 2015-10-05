<!DOCTYPE html>
<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="search.SearchAndBrowseCategory"%>
<%@page import="common.fedora.DatastreamID"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Map</title>

<!-- Bootstrap Core CSS -->
<link
	href="${pageContext.request.contextPath}/bootstrap-3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/bootstrap-3.3.5/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/typeahead.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		var words = "/data/movie.json";
		var countries = new Bloodhound({
			datumTokenizer : Bloodhound.tokenizers.whitespace,
			queryTokenizer : Bloodhound.tokenizers.whitespace,
			limit : 5,
			prefetch : {
				url : words,
			}
		});
		$('#prefetch .typeahead').typeahead(null, {
			name : 'countries',
			source : countries
		});
	});
</script>
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAkDhZAIvYefYnMLplLMYFBxACznT-8lmA"></script>
    <!--<script src="${pageContext.request.contextPath}/js/markerclusterer.js" type="text/javascript"></script>-->
<script>
var map;
function initialize() {
	var mapOptions ={
			zoom: 7,
			center: {lat: -34, lng: 18.5},
	}
  
	var contextPath='<%=request.getContextPath()%>';
	var placed=0
	var infowindow = new google.maps.InfoWindow({
	      content: '<!DOCTYPE html>'+
	    	  '<html>'+
	      '<body>'+
      
	      '<form method="post" action="${pageContext.request.contextPath}/archives/maps/done">'+
		'<input type="submit" value="Submit"/>'+
		'</form>'+

	      '</body>'+
	      '</html>'
	  });

	map = new google.maps.Map(document.getElementById('map-canvas'), mapOptions);
	var markers = [];
	
	var clicked=0;
	  
	<%Set<FedoraDigitalObject> coord = (HashSet<FedoraDigitalObject>) request.getAttribute("objects");
	 
	for(FedoraDigitalObject object: coord){
		System.out.println(object.getPid());
		String pid =object.getPid();
		String coverage = ((DublinCoreDatastream) object.getDatastreams().get("DC")).getCoverage();
		System.out.println(coverage);
		//String[] splitcoverage = coverage.split("%");
		//System.out.println(splitcoverage[0]);
		String[] latLng = coverage.split(",");
		System.out.println(latLng[0]);
		System.out.println(latLng[1]);
		
		Float lat=Float.parseFloat(latLng[0]);
		Float lng=Float.parseFloat(latLng[1]);
		%>
		
		
		
		var image1 = {url:'<%=object.getDatastreams().get("IMG").getContent()%>', size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
		
		var marker1 = new google.maps.Marker({
			position:  {lat: <%=lat%>, lng: <%=lng%>},
			map: map,
			icon: image1,
	      	title: "<%=((DublinCoreDatastream) object.getDatastreams().get("DC")).getTitle()%>"});
		
	  	markers.push(marker1);
	  	
	  	//google.maps.event.addListener(marker1, 'click', function() {
	  	    //infowindow.open(map,marker1);
	  	    //clicked= marker1
	  	    //<%request.setAttribute("view", object);%>
	  	    //<%System.out.println(object);%>
	  	    
	  	    /////////////
	  	    ///lighbox//
	  	    ////////////
	  	  //});
	 <%}
		 %>
	
 	console.log(placed)
 	<%System.out.println(request.getAttribute("placement"));%>
 	var thing=<%=request.getAttribute("placement")%>;
 	placed=thing;
  	
  	
  	google.maps.event.addListener(map, 'click', function(e) {
  		
  		if (placed==0){
  		console.log(placed)
  		var newimage = {url: '<%=((FedoraDigitalObject)request.getAttribute("digitalObject")).getDatastreams().get("IMG").getContent()%>', size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	  	var newmarker = new google.maps.Marker({
  	      	position:  e.latLng,
  	      	map: map,
  	      	draggable: true,
  	      	icon: newimage,
  	      	title: 'Hello World!'});
  		
  		google.maps.event.addListener(newmarker, "dragend", function(event) { 
  			infowindow.open(map,newmarker)
  			
  			var lat = event.latLng.lat(); 
            var lng = event.latLng.lng(); 
  			var latlng=lat+","+lng;
  			
  			document.getElementById("demo").innerHTML = latlng;
  			
          }); 
  		
  	  	console.log("clicked");
  	  	markers.push(newmarker);
  	  	placed=1;}
  	  });
  	
  //var mcOptions = {gridSize: 50, maxZoom: 15};
  //var markers = [marker1, marker2, marker3, marker4];
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>

</head>
<textarea id="demo" name="latlng" readonly=readonly> </textarea>
<body>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top navbar-left"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="#">Movie Snaps</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a
						href="${pageContext.request.contextPath}/archives/browse">Browse</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_exhibitions">Exhibitions</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_maps">Maps</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_uploads">Uploads</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_downloads">Downloads</a></li>
				</ul>
				<!-- search components-->
				
				<div id="bs-example-navbar-collapse-1"
					class="collapse navbar-collapse">

					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a
							href="${pageContext.request.contextPath}/archives/search_objects"
							class="dropdown-toggle" data-toggle="dropdown" role="button"
							aria-haspopup="true" aria-expalinded="false">${searchCategories[0]}<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:forEach var="searchCategory" items="${searchCategories}">
									<li><a
										href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>

								</c:forEach>
							</ul></li>
						<li>
							<form class="navbar-form navbar-right"
								action="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategories[0]}"
								method="post">
								<div class="form-group">
									<div id="prefetch">
										<input
											class="form-control typeahead tt-query tt-hint tt-dropdown-menu tt-suggestion"
											data-provider="typeahead" type="text"
											placeholder="Search Archive" autocomplete="off"
											spellcheck="false" name="terms">
									</div>
								</div>

								<button type="submit" class="btn">Search</button>

								<div class="checkbox">
									<label> <input type="checkbox" id="limitSearch"
										name="limitSearch" value="limitSearch"><font
										color="white"> Limit search to these results</font>
									</label>
								</div>

							</form>
						</li>
					</ul>
				</div>
			</div>
			<!-- end of search bar components -->
		</div>
	</nav>


	
				 <div id="map-canvas"></div>
	
	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid"
		role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}">Personal
					Histories</a>
			</div>
		</div>
		<!-- /.container -->
	</nav>
	<!-- /.container -->


	<!-- Script to Activate the Carousel -->
	<script>
		$('.carousel').carousel({
			interval : 5000
		//changes the speed
		})
	</script>

</body>

</html>