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

<link type="text/css" href="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />

	<script src="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.min.js"></script>
<script>
	$(document).ready(function(){
		
	
 		 $('.slider4').bxSlider({
   		 slideWidth: 210,
   		 minSlides: 2,
   	 	 maxSlides: 3,
   		 moveSlides: 2,
   	 	slideMargin: 8,
   	 	infiniteLoop:false,
		hideControlOnEnd:true
  		});});
		
		$('#prefetch .typeahead').typeahead(null, {
			name : 'countries',
			source : countries
		});
	
	
</script>
<style>
.droptargetCart {
 		margin: 5px;
    		padding: 5px;
   		}
		.droptargetTemplate {
   			width: 210px; 
    			height: 210px;
    			border: 1px dotted #aaaaaa;
    			background: url(${pageContext.request.contextPath}/images/cameraIcon1.png);
    	}
		.nodroptargetTemplate {
    		width: 100px; 
    		height: 200px;
    		margin: 15px;
    		padding: 10px;
			display:none;
    	}
		.slide{
			height:490px;
		}
		.caption{
			margin:2px;
		}
    	
    	div.scroll{
    		width: 240px;
    		height:500px;
    		overflow: scroll;
    	}
    	div.scroll img{
    		padding:10px;
    	}
		
	</style>
<script>
	function dragStart(event) {
    	event.dataTransfer.setData("Text", event.target.id);   
	}
	function allowDrop(event) {
    	event.preventDefault(); 
	}
	
	function drop(event) {	
    	event.preventDefault();
    	var a=event.target.id;
    	if(a.indexOf("http://")>-1){
    		//do nothing because block is occupied
    	}
    	else{
    		var data = event.dataTransfer.getData("Text");
    		event.target.appendChild(document.getElementById(data));
    	
			if(document.getElementById(data).style.width=="40%"){
				document.getElementById(data).style.width="100%";
				document.getElementById("demo").innerHTML = document.getElementById("demo").value + event.target.id + " " + data + " " ;		
			}
		
			else if (event.target.id=="cart"){
				document.getElementById(data).style.width="40%";
				document.getElementById("demo").innerHTML = document.getElementById("demo").value + "REMOVE " + data + " " ;
			}
			else if (document.getElementById(data).style.width=="100%"){
				document.getElementById("demo").innerHTML = document.getElementById("demo").value + "REMOVE " + data + " " + event.target.id + " " + data + " ";
			}
    	}
	}
</script>

</head>

<body>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top navbar-left"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}/archives/SequinsSelfAndStruggle">Sequins, Self and Struggle</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a
						href="${pageContext.request.contextPath}/archives/browse">Browse</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_exhibitions">Exhibitions</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_maps">Maps</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_uploads">Uploads</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_downloads">Downloads</a></li>
				</ul>
				<!-- search components-->
				<div id="bs-example-navbar-collapse-1"
					class="collapse navbar-collapse">

					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a
							href="${pageContext.request.contextPath}/archives/search_objects"
							class="dropdown-toggle" data-toggle="dropdown" role="button"
							aria-haspopup="true" aria-expalinded="false">${searchCategories[0]}<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:forEach var="searchCategory" items="${searchCategories}">
									<li><a
										href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>

								</c:forEach>
							</ul></li>
						<li>
							<form class="navbar-form navbar-right"
								action="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategories[0]}"
								method="post">
								<div class="form-group">
									<div id="prefetch">
										<input
											class="form-control typeahead tt-query tt-hint tt-dropdown-menu tt-suggestion"
											data-provider="typeahead" type="text"
											placeholder="Search Archive" autocomplete="off"
											spellcheck="false" name="terms">
									</div>
								</div>

								<button type="submit" class="btn">Search</button>

								<div class="checkbox">
									<label> <input type="checkbox" id="limitSearch"
										name="limitSearch" value="limitSearch"><font
										color="white"> Limit search to these results</font>
									</label>
								</div>

							</form>
						</li>
					</ul>


				</div>
			</div>
			<!-- end of search bar components -->

		</div>

	</nav>


	<div class="container">
	<p> </p><br><br><br>
		<div class="row" >
			<div class="col-sm-4">
			
			

				<%@ page import="common.fedora.FedoraException"%>
				<%@ page import="common.fedora.FedoraClient"%>
				<%@ page import="common.fedora.DublinCore"%>
				<%@ page import="common.fedora.FedoraDigitalObject"%>
				<%@ page import="search.FedoraCommunicator"%>
				<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
			<textarea id="demo" name="user_action" readonly=readonly style="display:none;"></textarea>
			<input type="submit" value="Back" class="btn btn-primary btn-sm" name="exhibition_det"/>
			
			<%String [] cartImages= (String[]) (session.getAttribute("MEDIA_CART")); 
			FedoraCommunicator fc= new FedoraCommunicator();
			FedoraDigitalObject ob;%>
			<div id="cart" class="scroll droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)" style="border: 1px solid #aaaaaa; diplay:inline-block; background:#F8F8F8;">
				<%for (int i=0;i<cartImages.length;i++){ 
					if(cartImages[i]!=null){
						ob=null;
					ob=fc.populateFedoraDigitalObject(cartImages[i]);%>
  					<img  src="<%=ob.getDatastreams().get("IMG").getContent() %>"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="<%=cartImages[i]%>">
  				<%}} %>	
				
			</div>
		</div>
		
		<div class="col-sm-7">	
  		<div class="slider4">
			<div class="slide">
					<div class="droptarget droptargetTemplate" id="0" ondrop="drop(event)" ondragover="allowDrop(event)">
					</div>
					<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap0" style="display:none;" placeholder="Add text to me"></textarea>
					</div>
					<div class="droptarget nodroptargetTemplate" id="1">
					</div>
					<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap1" placeholder="Add text to me"></textarea>
					</div>
			</div>
			<div class="slide">
					<div class="droptarget nodroptargetTemplate" id="2">
					</div>
					<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap2" placeholder="Add text to me"></textarea>
					</div>
					<div class="droptarget droptargetTemplate" id="3" ondrop="drop(event)" ondragover="allowDrop(event)">
					</div>
					<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap3" style="display:none" placeholder="Add text to me"></textarea>
					</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="4" ondrop="drop(event)" ondragover="allowDrop(event)" ">
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap4" style="display:none" placeholder="Add text to me"></textarea>
					</div>
				<div class="droptarget nodroptargetTemplate" id="5" >
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap5" ></textarea>
				</div>
			</div>	
			<div class="slide">
				<div class="droptarget nodroptargetTemplate" id="6" >
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap6" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate" id="7" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap7" style="display:none"></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="8" ondrop="drop(event)" ondragover="allowDrop(event)" >	
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap8" style="display:none" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget nodroptargetTemplate" id="9" >
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap9" placeholder="Add text to me"></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget nodroptargetTemplate" id="10" >
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap10" placeholder="Add text to me"></textarea>
					</div>
				<div class="droptarget droptargetTemplate" id="11" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
						<textarea maxlength="200" class="TextArea" rows="10" cols="26"  name="input_cap11" style="display:none" placeholder="Add text to me"></textarea>
				</div>
			</div>
		</div>
		</div>
		<div class="col-sm-1">
			 <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModal">Next</button>
		</div>
		
	
			
		
			<div class="modal fade" id="myModal" role="dialog" style="z-index:30000">
    			<div class="modal-dialog">
    				<div class="modal-content">
       					 <div class="modal-header">
          						<button type="button" class="close" data-dismiss="modal">&times;</button>
          						<h4 class="modal-title">Add exhibition details</h4>
        				</div>
        				<div class="modal-body">
          					
								<div class="form-group">
      								<label for="usr">Title</label>
      								<input type="text" name="Title" class="form-control">
   								</div>
							<div class="form-group">
      							<label for="usr">Description</label>
      							<input type="text" name="Description" class="form-control">
    						</div>
		 					<div class="form-group">
      							<label for="usr">Creator</label>
      							<input type="text" name="Creator" class="form-control">
    						</div>
								

        				</div>
        				<div class="modal-footer">
          					<input type="submit" value="Save" class="btn btn-primary btn-sm" name="exhibition_det"/>
         
       			 		</div>
       			 		
      			</div>
     
    		</div>
  </div>
					
			
		

</form>
				



			</div>
		</div>
		<!-- /.row -->
	</div>
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









