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
    <script src="markerclusterer.js" type="text/javascript"></script>
    <script>
var map;
function initialize() {
	var mapOptions ={
			zoom: 7,
			center: {lat: -34, lng: 18.5},
	}
  
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
	
	var image1 = {url: "images/1.jpg", size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
	var marker1 = new google.maps.Marker({
		position:  {lat: -34, lng: 18.5},
      //map: map,
      //icon: "images/1.jpg",
      icon: image1,
      	title: 'Hello World!'});
	
  	markers.push(marker1);
  	
  	google.maps.event.addListener(marker1, 'click', function() {
  	    infowindow.open(map,marker1);
  	  });
  
  	var image2 = {url: "images/2.jpg",  size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	var marker2 = new google.maps.Marker({
      position:  {lat: -34.1, lng: 18.6},
      //map: map,
      //icon: "images/2.jpg",
      icon: image2,
      title: 'Hello World!'});
  
  	markers.push(marker2);
  	
  	var image3 = {url: "images/3.jpg",  size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	var marker3 = new google.maps.Marker({
  		position:  {lat: -34.2, lng: 18.5},
      //map: map,
      //icon: "images/3.jpg",
      icon: image3,
      	title: 'Hello World!'});
  
  	markers.push(marker3);
  	
  	var image4 = {url: "images/4.jpg", size: null, origin: null, anchor: null, scaledSize: new google.maps.Size(40, 40)}
  	var marker4 = new google.maps.Marker({
      	position:  {lat: -34, lng: 18.4},
      //map: map,
      //icon: "images/4.jpg",
      icon: image4,
      	title: 'Hello World!'});
  	
  	markers.push(marker4);
  	var mc = new MarkerClusterer(map, markers);
  	
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