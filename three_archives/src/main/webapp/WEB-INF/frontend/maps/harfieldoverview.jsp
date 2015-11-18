<!DOCTYPE html>
<%@page import="java.io.PrintWriter"%>
<%@page import="common.fedora.FedoraException" %>
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
		var archive ='<%=session.getAttribute("ARCHIVE_CONCAT")%>';
		var words = "/data/" + archive + ".json";
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
    <script type="text/javascript">

    var map;
    function initialize() {
      map = new google.maps.Map(document.getElementById('map-canvas'), {
        zoom: 17,
        center: {lat: -33.9863, lng: 18.4743}
      });
      //loop though text file with polygon paths
      var contextPath='<%=request.getContextPath()%>';
      <%List<String> polygons =(List<String>) request.getAttribute("points");%>
      <%List<String> collections =(List<String>) request.getAttribute("harfieldcollections");%>
      
      console.log('<%=polygons%>');
      var markers = [];
      var polys = [];
      var polys1 = "";
    var COLLECTION="";
      var infowindow = new google.maps.InfoWindow({
          content: '<!DOCTYPE html>'+
        	  '<html>'+
          '<body>'+
      
          '<form method="post" action="${pageContext.request.contextPath}/archives/browse">'+
          '<textarea id="demo" name="category" readonly=readonly>COLLECTION</textarea>'+
          '<textarea id="demo1" name="COLLECTION" readonly=readonly></textarea>'+
    	'<input type="submit" value="Explore"/>'+
    	'</form>'+

          '</body>'+
          '</html>'
      });

      
      //function deletePolygons()
      //{
    	//  for (var i = 0; i < polys.length; i++) {
    		//    polys[i].setMap(null);
    		  
    		  //  polys = [];  
      //}
      //}
      
      //function deleteMarkers() {
    	//  for (var j = 0; j < markers.length; j++) {
    	  //  markers[j].setMap(null);
    	  
    	    //markers = [];
    	  //}
    	//}
      ///////this wont work//////fix/////
      
      <%for (int l = 0; l < polygons.size(); l++) {
    	    String[] corners = polygons.get(l).split(";");
    	    System.out.println("in loop");%>
    	    
    	    var path=[];
    	    console.log("in loop");
    	    
    	    <%for (int m = 0; m<corners.length;m++){
    	    	String[] latlng = corners[m].split("&");%>
    	    
    	    
    	    var corner = new google.maps.LatLng(<%=Float.parseFloat(latlng[0])%>,<%=Float.parseFloat(latlng[1])%>);
    	    path.push(corner);
    	    
    	    <%}%>
    	    
    	    var har
    	    har = new google.maps.Polygon({
    	        paths: path,
    	        strokeColor: '#000000',
    	        strokeOpacity: 0.8,
    	        strokeWeight: 2,
    	        fillColor: '#FFFFFF',
    	        fillOpacity: 0.35
    	      });

    	      har.setMap(map);

    	      google.maps.event.addListener(har, 'mouseover', function() {
    	    		console.log('mouseover')  
    	    		this.setOptions({fillColor:'#00FF00'});
    	    	  });

    	    google.maps.event.addListener(har, 'mouseout', function() {
    	    		console.log('mouseout')  
    	    		this.setOptions({fillColor:'#FFFFFF'});
    	    	  });
    	    
    	    google.maps.event.addListener(har, 'click', function(e) {
          		console.log('clicked')
          		this.setOptions({fillColor:'#FFCC00'});
          		var marker = new google.maps.Marker({
          		    position: {lat: e.latLng.lat(), lng:  e.latLng.lng()},
          		    map: map,
          		    //title: 'Uluru (Ayers Rock)'
          		  });
          		infowindow.open(map,marker);
          		marker.setVisible(false);
          		document.getElementById("demo1").innerHTML = "<%=collections.get(l)%>";
          	  });
    	    
    	     
    <%}%>
    //////////////setup from db////////////////// 


      /////////////////////////////////////////
      
      
      var rightclicks = 0
      var coords;
      var points;
      var coords1;
      google.maps.event.addListener(map, 'rightclick', function(e) {
    	    if (rightclicks == 0)
    	    {
    	    	rightclicks++;
    	    	console.log("start");
    	    	coords = "";
    	    	coords1 = [];
    	    	points="";

    	    }
    	    else if (rightclicks == 1)
    	    	{//console.log(coords);
    	    	
    	    	var har
    	    	har = new google.maps.Polygon({
                    paths: coords1,
                    strokeColor: '#000000',
                    strokeOpacity: 0.8,
                    strokeWeight: 2,
                    fillColor: '#FFFFFF',
                    fillOpacity: 0.35
                  });

                  har.setMap(map);
    	    	
                  google.maps.event.addListener(har, 'mouseover', function(e) {
              		console.log('mouseover')  
              		this.setOptions({fillColor:'#00FF00'});
              	  });
                
                google.maps.event.addListener(har, 'mouseout', function(e) {
              		console.log('mouseout')  
              		this.setOptions({fillColor:'#FFFFFF'});
              	  });
                
                console.log(points); 
    	    	polys.push(har);
    	    	polys1=polys1.concat(points, "\n");
                console.log("end");
                console.log("before delete: "+polys1);
    	    	//deleteMarkers();
    	    	//console.log("writing to file: " + polys1); 
    	    	//loadXMLDoc(points);
    	    	console.log("before demo: "+polys1);
    	    	document.getElementById("demo").innerHTML = polys1;
    	    	//console.log(points);
    	    	console.log("in demo: "+polys1);
    	    	//writeToFile(coords1)
    	    	rightclicks=0;}
    	  });
      
      google.maps.event.addListener(map, 'click', function(e) {
    	  //placeMarker(e.latLng, map);
    	  coords = coords.concat("new google.maps.LatLng("+e.latLng.lat() + ", " + e.latLng.lng()+"),\n");
    	  points = points.concat(e.latLng.lat() + "&" + e.latLng.lng()+";");
    	  coords1.push(e.latLng);
    	  console.log("corner");
    	  
    	  placeMarker(e.latLng, map);
    	  //
    	  });
      
    }

    function placeMarker(position, map) {
    	  var marker = new google.maps.Marker({
    	    position: position,
    	    map: map,
    	    //icon: contextPath+"/images/dot.png"
    	  });
    	  //map.panTo(position);

    	}


    google.maps.event.addDomListener(window, 'load', initialize);

    </script>
</head>
<body>

	<!-- Navigation -->
	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top navbar-left"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand"
					href="${pageContext.request.contextPath}/archives/${ARCHIVE_CONCAT}">${ARCHIVE}</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<c:forEach var="service" items="${SERVICES}">
						<c:if test="${service.value!='' && service.value!= ' '}">

							<c:choose>
								<c:when test="${service.key eq 'Uploads'}">
									<%--if (session.getAttribute("USER").equals("ADMINISTRATOR")){--%>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<%--}--%>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<li>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
					<!--add user stuff here-->
					<%--if (session.getAttribute("USER").equals("ADMINISTRATOR")){--%>

					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_user">Users</a></li>
					<%--}--%>

				</ul>
				<c:if test="${not empty SERVICES['Browse']}">
					<ul class="nav navbar-nav navbar-right top-nav">
						<!-- <li class="dropdown"><a
							href="${pageContext.request.contextPath}/archives/search_objects"
							class="dropdown-toggle" data-toggle="dropdown" role="button"
							aria-haspopup="true" aria-expalinded="false">${searchCategories[0]}<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:set var="first" value="true"/>
								<c:forEach var="searchCategory" items="${searchCategories}">
								<c:if test="${first ne 'true'}">
									<li><a
										href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>
										</c:if>
								<c:set var="first" value="false"/>
								</c:forEach>
							</ul></li>
 -->


						<li>

							<form class="navbar-form navbar-right"
								action="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategories[0]}"
								method="post">
								<div class="form-group">
									<div id="prefetch">
										<input
											class="form-control typeahead tt-query tt-hint tt-dropdown-menu tt-suggestion"
											data-provider="typeahead" type="text"
											placeholder="Search Archive"
											autocomplete="off" spellcheck="false" name="terms">
									</div>
								</div>
								<button type="submit" class="btn btn-default">
									<i class="glyphicon glyphicon-search"></i>
								</button>

							</form>
						</li>
					</ul>
				</c:if>
			</div>
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
