<!DOCTYPE html>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.List"%>
<html>
  <head>
    <title>Simple Map</title>
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
  console.log(polygons);
  var markers = [];
  var polys = [];
  var polys1 = "";
 
  function deletePolygons()
  {
	  for (var i = 0; i < polys.length; i++) {
		    polys[i].setMap(null);
		  
		    polys = [];  
  }
  }
  
  function deleteMarkers() {
	  for (var j = 0; j < markers.length; j++) {
	    markers[j].setMap(null);
	  
	    markers = [];
	  }
	}
  
  <%for (int l = 0; l < polygons.size(); l++) {
	    polygons.get(l).split(";");
	     
}%>
   
  
  
  
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
	    	{console.log(coords);
	    	
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
             
	    	polys.push(har);
	    	polys1.concat(points, ";");
            console.log("end");
	    	deleteMarkers();
	    	console.log("writing to file"); 
	    	//loadXMLDoc(points);
	    	document.getElementById("demo").innerHTML = polys1;
	    	console.log(points)
	    	//writeToFile(coords1)
	    	rightclicks=0;}
	  });
  
  google.maps.event.addListener(map, 'click', function(e) {
	  //placeMarker(e.latLng, map);
	  coords = coords.concat("new google.maps.LatLng("+e.latLng.lat() + ", " + e.latLng.lng()+"),\n");
	  points = points.concat("lat: " +e.latLng.lat() + ", long: " + e.latLng.lng()+";");
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
	  map.panTo(position);

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