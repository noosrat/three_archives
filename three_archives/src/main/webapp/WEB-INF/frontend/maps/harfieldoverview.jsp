<!DOCTYPE html>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.List"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="common.fedora.FedoraException" %>
<html>
  <head>
    <title>Map</title>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      html, body, #map-canvas {
        height: 95%;
        width: 100%;
        margin: 0px;
        padding: 0px
      }
      #map_canvas {
    position: relative;
}
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAkDhZAIvYefYnMLplLMYFBxACznT-8lmA"></script>
    <script>
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
    <div id="map-canvas"></div>
	<form method="post" action="${pageContext.request.contextPath}/archives/maps/add_polygon">
		<textarea id="demo" name="polypoints" readonly=readonly style="display:none;"> </textarea>
			<input type="submit" value="Sumbit Changes" name="sendPoints"/>
		</form>
	<input onclick="deletePolygons();" type=button value="Cancel">
  </body>
</html>