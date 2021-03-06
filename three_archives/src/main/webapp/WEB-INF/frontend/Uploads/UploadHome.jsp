<!DOCTYPE html>
<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="search.SearchAndBrowseCategory"%>
<%@page import="common.fedora.DatastreamID"%>

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

<title>Personal Histories - Centre for Curating The Archive</title>

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
	<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		$('[data-toggle="tooltip"]').tooltip(); 
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
<style>
	.thumb {
    			height: 100px;
    			border: 1px solid #000;
    			margin: 10px 5px 0 0;
  		}
		#instruction{
			
			background-color:#ffffff;
			display:none;
			width:300px;
			
			
			margin-top:345px;
			opacity:0.9;
			
		}

		#ImageStore{
			width:250px;
			height:500px;
			background-color:#ffffff
			margin:auto;
			padding:2px;
			overflow:auto;
			display:none;
				
		}
		.inputs{
			width:100%;
		}	
		
</style>
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
									<% if (session.getAttribute("USER")!=null){
										if (session.getAttribute("USER").equals("ADMINISTRATOR")){%>
									
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<%}}%>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<li>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
					<%if (session.getAttribute("USER")!=null){
						if (session.getAttribute("USER").equals("ADMINISTRATOR")){%>
					

					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_user">Users</a></li>
					<%}}%>
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

	<div class="container-fluid">
		<p></p><br><br>
		<div class="row">
			<div class="col-lg-3" style="background-color:#F5FFFF; box-shadow: 5px 0 5px -2px #888;min-height: 700px; padding-left:30px;">
			 
				<div>		
					<form method="post" action="${pageContext.request.contextPath}/archives/upload_uploads" enctype="multipart/form-data">
						<textarea id="able_selected_image" style="display:none">false</textarea>
						<textarea id="actions" name="actions" style="display:none"></textarea>
					<br><br>
  					<div class="panel-group">
    						<div class="panel panel-default">
      							<a data-toggle="collapse" href="#collapse1"><div class="panel-heading">
        							<h4 class="panel-title">
          								Get started
        							</h4></a>
      							</div>
     							 <div id="collapse1" class="panel-collapse collapse">
        							<div class="panel-body">
									<div id="select_files" >

					<h4>Select Archive</h4>
					<div>
						<input type="radio" name="archive" value="harfield_village" required>Harfield Village
						<input type="radio" name="archive" value="snaps" required>Movie Snaps<br>	
						<input type="radio" name="archive" value="miss_gay" required>Miss Gay WC
						<input type="radio" name="archive" value="spring_queen" required>Spring Queen<br>
					</div>
					<div>
		
					<br>
					<div id="file_input">
						<output id="list"></output> 
						<input type="file" id="files" name="files[]" multiple/><br>
					</div>
					<button class="btn btn-primary btn-sm" style="float:center;margin-left:150px" id="nextButton" type="button" onClick="makeClickable()">Add metadata</button>

					
					<br>
					</div>
					
				
				</div>



								</div>
        							
      							</div>
    						</div>
  					</div>
				
			</div>
			<div style="display:inline;list-style-type:none;float:left;">
					<div id="ImageStore"></div>
					</div>
					<p> </p><br><br><br>
		</div>
	<div class="col-lg-7" style="margin-left:10px; background-color:#FFFFFF;">
		
			<div id="instruction" >
				<h3><span class="glyphicon glyphicon-hand-left"></span> Select image to add metadata</h3>
				<br><br>
			</div>
		 	
			<div id="metadataForm" style="visibility:hidden;">
				<table class="table table-bordered" style="width:80%;">
					<thead style="background:#cae6e6;">
						<th>Field</th><th>Value </th>
					</thead>
					<textarea id="selected_image" style="display:none"></textarea>
					<tbody style="background:#ffffff">
						
						<tr><td>Title:</td> <td><input class="inputs"  id="title" placeholder="A name given to the resource."></td></tr>
						<tr><td>Creator:</td><td> <input class="inputs" id="creator" placeholder="An entity primarily responsible for making the resource."></td></tr>
						<tr><td>Event:</td><td> <input class="inputs" id="event" placeholder="Event to which the object is related"></td></tr>
						<tr><td>Description:</td><td> <input class="inputs" id="description" placeholder="An account of the resource."></td></tr>
						<tr><td>Publisher:</td><td> <input class="inputs" id="publisher" placeholder="An entity responsible for making the resource available."></td></tr>
						<tr><td>Contributor:</td><td> <input class="inputs" id="contributor" placeholder="An entity responsible for making contributions to the resource."></td></tr>
						<tr><td>Date:</td><td> <input class="inputs" id="date" placeholder="A point or period of time associated with an event in the lifecycle of the resource."></td></tr>
						<tr><td>Resource type:</td><td> <input class="inputs" id="resourcetype" placeholder="The nature or genre of the resource."></td></tr>
						<tr><td>Source:</td><td> <input class="inputs" id="source" placeholder="A related resource from which the described resource is derived."></td></tr>
						<tr><td>Language:</td><td> <input class="inputs" id="language" placeholder="A language of the resource."></td></tr>
						<tr><td>Relation:</td><td> <input class="inputs" id="relation" placeholder="A related resource."></td></tr>
						<tr><td>Location:</td><td> <input class="inputs" id="location" placeholder="The spatial or temporal topic of the resource."></td></tr>
						<tr><td>co-ordinates:</td><td> <input class="inputs" id="cords" placeholder="The spatial or temporal topic of the resource"></td></tr>
						<tr><td>Rights:</td><td> <input class="inputs" id="rights" placeholder="Information about rights held in and over the resource."></td></tr>
						<tr><td>Collection:</td><td> <input class="inputs" id="collections" placeholder="The topic of the resource."></td></tr>
						<tr><td>Format:</td><td> <select id="format">
  									<option value="image/jpeg">Image</option>
  									<option value="video/mp4">Video</option>
  									<option value="audio/mp3">Audio</option>
  									
								</select> <button style="float:right;margin-left:5px" type="button" class="button btn btn-primary btn-xs" onClick="submitMetadata()">Update</button> <button type="button" style="float:right;" class="btn btn-primary btn-xs" onClick="cancelSelection()">Cancel</button></td></tr>
					</tbody>
				</table>
				<br><br><br>
			
			</div> 
		
	</div>	
	
	<div class="col-lg-1">
	<br><br>
		<input class="button btn btn-primary btn-sm" type="submit" value="Done" name="upload_files" id="done">
	</div>
</form>	



				</div>
			</div>
		</div>
		<!-- /.row -->
	</div>
	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid"
		role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}"><span class="glyphicon glyphicon-home"></span> Personal
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
<head>
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
		alert("Please complete current action before proceeding");
		
	}
	else if(document.getElementById(event).style.border=='5px solid green')
	{
		alert("this is already added");
	}
	else if (document.getElementById("able_selected_image").value!="false")
	{
				
		document.getElementById("able_selected_image").value="false"
		document.getElementById("instruction").style.display="none";
		document.getElementById("done").style.display="none";
		document.getElementById("metadataForm").style.visibility="visible";
		var x=document.getElementById("selected_image").value;	
		document.getElementById("selected_image").innerHTML =event;	
    		document.getElementById("selected_image").innerHTML =event;
    		document.getElementById(event).style.border= '10px solid black';
		
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
		document.getElementById("done").style.display="block";
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
	//document.getElementById("list").style.diplay="none";
	//document.getElementById("files").style.diplay="none";
	//document.getElementById("file_input").style.diplay="none";
	document.getElementById("nextButton").style.display="none";
	document.getElementById("able_selected_image").value="true"
	document.getElementById("instruction").style.display="block";
	//document.getElementById("files").style.display="none";
}

</script>
</head>
</html>