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
    zoom: 7,
    center: {lat: -34, lng: 18.5}
  });
  
  var image1 = {url: "images/1.jpg", size: new google.maps.Size(40, 40), origin: new google.maps.Point(0,0), anchor: new google.maps.Point(-40, 0)}
  var marker = new google.maps.Marker({
      position:  {lat: -34, lng: 18.5},
      map: map,
      //icon: "images/1.jpg",
      icon: image1,
      title: 'Hello World!'
  });
  
  var image2 = {url: "images/2.jpg", size: new google.maps.Size(40, 40), origin: new google.maps.Point(0,0), anchor: new google.maps.Point(-40, 0)}
  var marker1 = new google.maps.Marker({
      position:  {lat: -34.1, lng: 18.6},
      map: map,
      //icon: "images/2.jpg",
      icon: image2,
      title: 'Hello World!'
  });
  
  var image3 = {url: "images/3.jpg", size: new google.maps.Size(40, 40), origin: new google.maps.Point(0,0), anchor: new google.maps.Point(-40, 0)}
  var marker2 = new google.maps.Marker({
      position:  {lat: -34.2, lng: 18.5},
      map: map,
      //icon: "images/3.jpg",
      icon: image3,
      title: 'Hello World!'
  });
  
  var image4 = {url: "images/4.jpg", size: new google.maps.Size(40, 40), origin: new google.maps.Point(0,0), anchor: new google.maps.Point(-40, 0)}
  var marker3 = new google.maps.Marker({
      position:  {lat: -34, lng: 18.4},
      map: map,
      //icon: "images/4.jpg",
      icon: image4,
      title: 'Hello World!'
  });
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <body>
    <div id="map-canvas"></div>
  </body>
</html>