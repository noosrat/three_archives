
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
 <script src="${pageContext.request.contextPath}/jquery.js"></script>

    
<script>

    // Scrolls to the selected menu item on the page
    $(function() {
        $('a[href*=#]:not([href=#])').click(function() {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') || location.hostname == this.hostname) {
                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: target.offset().top
                    }, 1000);
                    return false;
                }
            }
        });
    });
    

</script>	
<style>
  		.thumb {
    			height: 120px;
    			border: 1px solid #000;
    			margin: 10px 5px 0 0;
  		}
		#instruction{
			border-radius:10px;
			border: 2px dashed #000;
		}

		#ImageStore{
			width:300px;
			height:700px;
			//width:700px;
			//height:600;
			//border: 1px solid #000;
			margin:10px;
			padding:2px;
			overflow:auto;
			display:none;	
		}
		.inputs{
			width:100%;
		}		
		
	</style>
<body>
<form method="post" action="${pageContext.request.contextPath}/archives/upload_uploads">
	<div class="page-header">
  		<h1>Upload</h1>
		<textarea id="able_selected_image">false</textarea>
		<textarea id="actions" name="actions"></textarea>
	</div>
	<nav class="navbar navbar-default">
  		<div class="container-fluid">
    		<div class="navbar-header">
      			<a class="navbar-brand" href="#">Personal Histories</a>
    		</div>
    		<div>
      		<ul class="nav navbar-nav">
        		<li class="active"><a href="#">Home</a></li>
			<li><a href="#">About</a></li>
			<li><a href="#">Research</a></li>
        		<li><a href="#">Exhibitions</a></li>
        		<li><a href="#">Maps</a></li>
        		<li><a href="#">Browse</a></li>
      		</ul>
   		 </div>
  		</div>
	</nav> 
	<div class="container" style="background-color:#fcecdf">
		<br>
	<div id="select_files" style="margin:auto; width:400px; background-color:#FCFFFC;border-radius: 25px;padding:10px;padding-left:80px;border: 2px dashed #333300;">
				<br>
				<input type="radio" name="archive" value="Harfield" required>Harfield Village<br>
				<input type="radio" name="archive" value="Snaps" required>Movie Snaps<br>
				<input type="radio" name="archive" value="MissGay" required>Miss Gay WC<br>
				<input type="radio" name="archive" value="SpringQueen" required>Spring Queen<br><br>
				Enter image path (eg.C:/images/): <input type="text" name="storage_path" id="storage_path" required placeholder="C:/images/"><br><br>
				<output id="list"></output> 
				
				<input type="file" id="files" name="files[]" multiple /><br>
				<a href="#meta" <button class="btn-success" style="float:center;margin-left:100px" id="nextButton" type="button" onClick="makeClickable()" >Next</button></a><br><br>
	</div>
	<section id="meta" > 	
	<br>	
	<ul style="list-style-type:none;display:inline;margin:0;padding:0;background-color:#fbe3cf">
		<li style="display:inline;list-style-type:none;float:left;">
			<div id="ImageStore"></div>
		</li>
		
		<li style="display:inline;list-style-type:none;">	
			<div id="instruction" style="background-color:#ffffff;display:none;width:300px;text-align:center;margin-top:200px; margin-left:400px;">
				<h2>Select image to add metadata</h2>
				
  					<input type="submit" value="No thanx, I'm done" name="upload_files">
					
				
				<br><br>
			</div>
		 	
			<div id="metadataForm" style="visibility:hidden;margin-top:10px;">
				<table class="table table-bordered" style="width:50%;">
					<thead style="background:#fadabf;">
						<th>Field</th><th>Value </th>
					</thead>
					<tbody style="background:#ffffff">
						<tr><td>Filename:</td><td> <textarea id="selected_image"></textarea></td></tr>
						<tr><td>Title:</td> <td><input class="inputs"  id="title"></td></tr>
						<tr><td>Creator:</td><td> <input class="inputs" id="creator"></td></tr>
						<tr><td>Event:</td><td> <input class="inputs" id="event"></td></tr>
						<tr><td>Description:</td><td> <input class="inputs" id="description"></td></tr>
						<tr><td>Publisher:</td><td> <input class="inputs" id="publisher"></td></tr>
						<tr><td>Contributor:</td><td> <input class="inputs" id="contributor"></td></tr>
						<tr><td>Date:</td><td> <input class="inputs" id="date"></td></tr>
						<tr><td>Resource type:</td><td> <input class="inputs" id="resourcetype"></td></tr>
						<tr>
							<td>Format:</td>
							<td> 
								<select id="format">
  									<option value="image/jpeg">Image</option>
  									<option value="video/mp4">Video</option>
  									<option value="audio/mp3">Audio</option>
  									
								</select>
							</td>
						</tr>
						<tr><td>Source:</td><td> <input class="inputs" id="source"></td></tr>
						<tr><td>Language:</td><td> <input class="inputs" id="language"></td></tr>
						<tr><td>Relation:</td><td> <input class="inputs" id="relation"></td></tr>
						<tr><td>Location:</td><td> <input class="inputs" id="location"></td></tr>
						<tr><td>co-ordinates:</td><td> <input class="inputs" id="cords"></td></tr>
						<tr><td>Rights:</td><td> <input class="inputs" id="rights"></td></tr>
						<tr><td>Collection:</td><td> <input class="inputs" id="collections"></td></tr>
						<tr><td></td><td>  <button type="button" onClick="cancelSelection()">Cancel</button><button type="button" onClick="submitMetadata()">Update</button></td></tr>
					</tbody>
				</table>
			
			</div> 
		</li>
		
	</ul>
	</section>
	
	</div>	
</form>		
</body>


<script>

  function handleFileSelect(evt) {
    var files = evt.target.files; // FileList object 
    for (var i = 0, f; f = files[i]; i++) 
    {// Loop through the FileList and render image files as thumbnails.
      if (f.type.match('image.*')) 
      {// process image files.
    	  var reader = new FileReader();
		   reader.onload = (function(theFile) {// Closure to capture the file information.
            return function(event) {
              // Render thumbnail.
              var span = document.createElement('span');
    		document.getElementById("ImageStore").style.display="block";	
              document.getElementById("ImageStore").innerHTML = document.getElementById("ImageStore").innerHTML+['<img class="thumb MGG" ; onclick="selectImage(this.id)" src="', event.target.result,'" id="', escape(theFile.name), '" title="', escape(theFile.name), '"/>'].join('');
              document.getElementById('list').insertBefore(span, null);
    	
            };
          })(f);

          // Read in the image file as a data URL.
          reader.readAsDataURL(f);
      }
    	
      if (f.type.match('video.*')) 
      {// process image files.
    	  var reader = new FileReader();
		   reader.onload = (function(theFile) {// Closure to capture the file information.
            return function(event) {
              // Render thumbnail of .
              var span = document.createElement('span');
    		document.getElementById("ImageStore").style.display="block";	
    		document.getElementById("ImageStore").innerHTML = document.getElementById("ImageStore").innerHTML+['<video class="thumb MGG" controls type="video/mp4" onclick="selectImage(this.id)" src="', event.target.result,'" id="', escape(theFile.name), '" title="', escape(theFile.name), '"/>'].join('');  
              document.getElementById('list').insertBefore(span, null);
    	
            };
          })(f);

          // Read in the image file as a data URL.
          reader.readAsDataURL(f);
      }
      
      if (f.type.match('audio.*')) {
          var reader = new FileReader();
        // Closure to capture the file information.
        reader.onload = (function(theFile) {
          return function(event) {
            // Render thumbnail.
            var span = document.createElement('span');
          	 document.getElementById("ImageStore").innerHTML = document.getElementById("ImageStore").innerHTML+['<audio class="thumb MGG" controls type="audio/mp3" onclick="selectImage(this.id)" src="', event.target.result,'" id="', escape(theFile.name), '" title="', escape(theFile.name), '"/>'].join('');  
  			document.getElementById('list').insertBefore(span, null);
  	
          };
        })(f);

        // Read in the image file as a data URL.
        reader.readAsDataURL(f);
        }

      
    }
  }

  document.getElementById('files').addEventListener('change', handleFileSelect, false);
</script>

<script>

function selectImage(event) {
	
	if(document.getElementById("able_selected_image").value=="false")
	{
		alert("First Complete current image");
		
	}
	else if(document.getElementById(event).style.border=='5px solid green')
	{
		alert("this is already added");
	}
	else if (document.getElementById("able_selected_image").value!="false")
	{			
		document.getElementById("able_selected_image").value="false"
		document.getElementById("instruction").style.display="none";
		document.getElementById("metadataForm").style.visibility="visible";
		var x=document.getElementById("selected_image").value;	
		document.getElementById("selected_image").innerHTML =event;	
    		document.getElementById("selected_image").innerHTML =event;
    		document.getElementById(event).style.border= '10px solid green';
	}
	
}

function submitMetadata(){
	
	var filename= document.getElementById("selected_image").value;
	var title=document.getElementById("title").value;
	var creator=document.getElementById("creator").value;
	var event=document.getElementById("event").value;
	var description=document.getElementById("description").value;
	var publisher=document.getElementById("publisher").value;
	var contributor=document.getElementById("contributor").value;
	var date=document.getElementById("date").value;
	var resourcetype=document.getElementById("resourcetype").value;
	var format=document.getElementById("format").value;
	var source=document.getElementById("source").value;
	var language=document.getElementById("language").value;
	var relation=document.getElementById("relation").value;
	var location=document.getElementById("location").value;
	var rights=document.getElementById("rights").value;
	var collections=document.getElementById("collections").value;
	var cords=document.getElementById("cords").value;

	var doCheck=filename+title+creator+event+description+publisher+contributor+date+resourcetype+format+source+language+relation+location+rights+collections+cords;
	if(doCheck.indexOf('%') > -1 )
	{
		alert("'%' is not a valid character. Please remove it from text.");
	}
	else{
		document.getElementById(filename).style.display="none";
		document.getElementById("actions").innerHTML=document.getElementById("actions").value+" "+filename+"% "+title+"% "+creator+"% "+event+"% "+ description+"% "+  publisher+"% "+  contributor +"% "+ date+"% "+  resourcetype+"% "+  format+"% "+  source+"% "+  language+"% "+  relation+"% "+  location +"% "+ rights+"% "+collections+"% "+cords+"%%";
		document.getElementById("able_selected_image").value="true";
		title=document.getElementById("title").value="";
		creator=document.getElementById("creator").value="";
		document.getElementById("metadataForm").style.visibility="hidden";
		document.getElementById("instruction").style.display="block";
	}
}
function cancelSelection(){
	var filename= document.getElementById("selected_image").value;
	document.getElementById(filename).style.border= '';
	document.getElementById("able_selected_image").value="true";
	title=document.getElementById("title").value="";
	document.getElementById("metadataForm").style.visibility="hidden";
	document.getElementById("instruction").style.display="block";

}

function makeClickable()
{
	document.getElementById("select_files").style.diplay="none";
	document.getElementById("nextButton").style.display="none";
	document.getElementById("able_selected_image").value="true"
	document.getElementById("instruction").style.display="block";
	document.getElementById("files").style.display="none";
}

</script>
</head>
	

</html>