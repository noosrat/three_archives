<!DOCTYPE html>
<html>
  <head>
    <title>Simple Map</title>
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
    <script>
var map;
function initialize() {
  map = new google.maps.Map(document.getElementById('map-canvas'), {
    zoom: 17,
    center: {lat: -33.9863, lng: 18.4743}
  });
  var harfield1;
  var triangleCoords1 = [new google.maps.LatLng(-33.98540458260216, 18.472459316253662),
                        new google.maps.LatLng(-33.9858760765136, 18.472448587417603),
                        new google.maps.LatLng(-33.98593834909882, 18.47424030303955),
                        new google.maps.LatLng(-33.985449063271574, 18.47427248954773),
                        new google.maps.LatLng(-33.98540458260216, 18.472459316253662)];

                      // Construct the polygon.
                      harfield1 = new google.maps.Polygon({
                        paths: triangleCoords1,
                        strokeColor: '#000000',
                        strokeOpacity: 0.8,
                        strokeWeight: 2,
                        fillColor: '#FFFFFF',
                        fillOpacity: 0.35
                      });

                      harfield1.setMap(map);
  
  google.maps.event.addListener(harfield1, 'mouseover', function(e) {
		console.log('mouseover')  
		this.setOptions({fillColor:'#00FF00'});
	  });
  
  google.maps.event.addListener(harfield1, 'mouseout', function(e) {
		console.log('mouseout')  
		harfield1.setOptions({fillColor:'#FFFFFF'});
	  });
  
  google.maps.event.addListener(harfield1, 'click', function(e) {
		console.log('click')  
		href=window.location.assign("detailviewer");
	  });
  
  var harfield2;
  var triangleCoords2 = [new google.maps.LatLng(-33.98636535988254, 18.472437858581543), 
                        new google.maps.LatLng(-33.98640984004913, 18.473961353302002),
                        new google.maps.LatLng(-33.98593834909882, 18.47424030303955),
                        new google.maps.LatLng(-33.9858760765136, 18.472448587417603),
                        new google.maps.LatLng(-33.98636535988254, 18.472437858581543)];

                      // Construct the polygon.
                      harfield2 = new google.maps.Polygon({
                        paths: triangleCoords2,
                        strokeColor: '#000000',
                        strokeOpacity: 0.8,
                        strokeWeight: 2,
                        fillColor: '#FFFFFF',
                        fillOpacity: 0.35
                      });

                      harfield2.setMap(map);
  
  google.maps.event.addListener(harfield2, 'mouseover', function(e) {
		console.log('mouseover')  
		this.setOptions({fillColor:'#00FF00'});
	  });
  
  google.maps.event.addListener(harfield2, 'mouseout', function(e) {
		console.log('mouseout')  
		harfield2.setOptions({fillColor:'#FFFFFF'});
	  });
  
  var rightclicks = 0
  var coords;
  google.maps.event.addListener(map, 'rightclick', function(e) {
	    if (rightclicks == 0)
	    {
	    	rightclicks++;
	    	console.log("start");
	    	coords = [];
	    	
	    }
	    else if (rightclicks == 1)
	    	{console.log(coords);
	    	console.log("end");
	    	rightclicks=0;}
	  });
  
  google.maps.event.addListener(map, 'click', function(e) {
	  //placeMarker(e.latLng, map);
	  coords.push("new google.maps.LatLng("+e.latLng.lat() + ", " + e.latLng.lng()+"),\n");
	  console.log(1+2);
	  //
	  });
  
}

function placeMarker(position, map) {
	  var marker = new google.maps.Marker({
	    position: position,
	    map: map
	  });
	  map.panTo(position);
	}


google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <body>
    <div id="map-canvas"></div>
  </body>
</html>