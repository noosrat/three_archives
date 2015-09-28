<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<title>Upload</title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
  	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  	<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
	<style>
  		.thumb {
   		 	height: 75px;
    		border: 1px solid #000;
    		margin: 10px 5px 0 0;
  		}
		#ImageStore{
			width:80%;
			height:100px;
			border: 1px solid #000;
			margin:10px;
			padding:2px;
			overflow:auto;
		}
		.MGG{
	
		}
	</style>

	<script>
  function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object

    // Loop through the FileList and render image files as thumbnails.
    for (var i = 0, f; f = files[i]; i++) {

      // Only process image files.
      if (!f.type.match('image.*')) {
        continue;
      }

      var reader = new FileReader();

      // Closure to capture the file information.
      reader.onload = (function(theFile) {
        return function(event) {
          // Render thumbnail.
          var span = document.createElement('span');
          document.getElementById("ImageStore").innerHTML = document.getElementById("ImageStore").innerHTML+['<img class="thumb MGG" onclick="myFunction(this.id)" src="', event.target.result,'" id="', escape(theFile.name), '" title="', escape(theFile.name), '"/>'].join('');
          document.getElementById('list').insertBefore(span, null);
	
        };
      })(f);

      // Read in the image file as a data URL.
      reader.readAsDataURL(f);
    }
  }
  document.getElementById('files').addEventListener('change', handleFileSelect, false);
  
  function myFunction(event) {
		
	    document.getElementById("demo").innerHTML = event;
	    document.getElementById(event).style.border= '10px solid';
		
	}
	</script>
</head>

<body>
	<input type="file" id="files" name="files[]" multiple />
	<output id="list"></output>
	<br>
	<br>	
	<h2>Select image to add metadata</h2>
	<div id="ImageStore"></div>
		<p id="demo"></p>
		<p style="visibility:hidden" id="IMAGE_TITLE">This is a p</p>
	
	
	
	
</body>
</html>