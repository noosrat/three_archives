<!DOCTYPE html>

<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<html>
  <head>
    <title>Map</title>
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

	      '<img src="images/1.jpg">'+
	      
	      '<a href="imageviewer?image=images/1.jpg">More Deatails...</a>'+

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
	
	
	/*'http://localhost:8080/fedora/objects/'+coord.get(0).getPid()+'/datastreams/'+coord.get(0).getPid()+'/content' /*contextPath+"/fedora/objects/ms:1/datastreams/ms1/content"*/
	
	
  
  	/*var image2 = {url: 'http://dreamatico.com/data_images/kitten/kitten-2.jpg',  size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	var marker2 = new google.maps.Marker({
      position:  {lat: -34.1, lng: 18.6},
      map: map,
      //icon: "images/2.jpg",
      icon: image2,
      title: 'Hello World!'});
  
  	markers.push(marker2);
  	
  	var image3 = {url: contextPath+"/images/kitten.jpg",  size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	var marker3 = new google.maps.Marker({
  		position:  {lat: -34.2, lng: 18.5},
      map: map,
      //icon: "images/3.jpg",
      icon: image3,
      	title: 'Hello World!'});
  
  	markers.push(marker3);
  	
  	var image4 = {url: contextPath+"/images/4.jpg", size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	var marker4 = new google.maps.Marker({
      	position:  {lat: -34, lng: 18.4},
      //map: map,
      //icon: "images/4.jpg",
      icon: image4,
      	title: 'Hello World!'});
  	
  	markers.push(marker4);*/
  	//var mc = new MarkerClusterer(map, markers);
 	console.log(placed)
  	
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
  <body>
    <div id="map-canvas"></div>
  </body>
</html>