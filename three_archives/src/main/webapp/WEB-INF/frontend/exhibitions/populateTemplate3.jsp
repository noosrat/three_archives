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
<script type="text/javascript" src="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.min.js"></script>
<script>
	$(document).ready(function(){
		$('[data-toggle="tooltip"]').tooltip(); 
 		 $('.slider4').bxSlider({
   		 slideWidth: 210,
   		 minSlides: 2,
   	 	 maxSlides: 3,
   		 moveSlides: 2,
   	 	slideMargin: 8,
   	 	infiniteLoop:false,
		hideControlOnEnd:true
  		});
		});

	
</script>
<style>
	
		.Sequins{
			width:150px;
			height:100px;
			background-color: #ffffff;
    			border: 1px solid black;
    			opacity: 0.8;	

		}
		h3{
			margin: 5%;
    			font-weight: bold;
    			color: #000000;
		}
</style>
<style>
		.droptargetCart {
 			margin: 5px;
    		padding: 5px;
   		}
		.droptargetTemplate {
			overflow:hidden;
   			width: 210px; 
    			height: 210px;
    			border: 1px dotted #aaaaaa;
    			background: url("${pageContext.request.contextPath}/images/cameraIcon1.png");
    	}
		.nodroptargetTemplate {
    		width: 100px; 
    		height: 200px;
    		margin: 15px;
    		padding: 10px;
    	}
		.slide{
			height:570px;
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
		#toolBox{
			height:auto;
			width:220px;
			text-align:center;
			padding:8px;
			border-radius:8px;
			background-color:#E0EBEB;
		}
		.thumb {
    			height: 100px;
    			border: 1px solid #000;
    			margin: 10px 5px 0 0;
  		}
		textarea{
			text-align:center;
			font-family:"Times New Roman", Times, serif;  
   			font-size: 14px; 
		}
	</style>
<script>
function selectBorder(event){
	document.getElementById(event).style.border= '10px solid blue';
	document.getElementById("border").value=event;	
}
function selectImage(event) {
	document.getElementById(event).style.border= '10px solid blue';
	document.getElementById("cover").value=event;	
}
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
									<%if (session.getAttribute("USER")!=null){
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
					<!--add user stuff here-->
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


	<div class="container">
	<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
	<p> </p><br><br><br>
		<div class="row" >
			<div class="col-sm-4">
			

				<%@ page import="common.fedora.FedoraException"%>
				<%@ page import="common.fedora.FedoraClient"%>
				<%@ page import="common.fedora.DublinCore"%>
				<%@ page import="common.fedora.FedoraDigitalObject"%>
				<%@ page import="search.FedoraCommunicator"%>
				<%@ page import="java.util.ArrayList"%>
				<%@ page import="java.util.List"%>
			<textarea id="cover" name="cover" readonly=readonly style="display:none;" ></textarea>
			<textarea id="border" name="border" readonly=readonly style="display:none;"></textarea>		
			<textarea id="demo" name="user_action" readonly=readonly style="display:none;"></textarea>
			<input type="submit" value="Back" class="btn btn-primary btn-sm" name="exhibition_det"/>
			<div id="toolBox">
					<h3>Toolbox</h3>
						<ul id="mainEx" style="list-style-type:none;margin:auto;padding:5px;text-align:center">
							<li style="display:inline;">
								
								<input  type="button" style= "width:25px;height:25px;background: url('${pageContext.request.contextPath}/images/icons/coverIcon.jpg') no-repeat;" data-toggle="modal" data-target="#backgroundImage"/>
							</li>
					
							<li style="display:inline; ">
								<span data-toggle="tooltip" title="Add a cover image to exhibition" class="glyphicon glyphicon-question-sign"></span>
							
							</li>

						</ul>
						<ul id="mainEx" style="list-style-type:none;margin:auto;padding:5px;text-align:center">
							<li style="display:inline;">
								<input type="button" style= "width:25px;height:25px;background: url('${pageContext.request.contextPath}/images/icons/borderIcon.jpg') no-repeat;" data-toggle="modal" data-target="#imageBorders"/>
							</li>
					
							<li style="display:inline; ">
								
								<span data-toggle="tooltip" title="Add borders to images" class="glyphicon glyphicon-question-sign"></span>
							</li>

						</ul>
						</div><br><br>
				<div class="modal fade" id="backgroundImage" role="dialog" style="z-index:30000">
    			<div class="modal-dialog">
    				<div class="modal-content">
       					 <div class="modal-header">
          						<button type="button" class="close" data-dismiss="modal">&times;</button>
          						<h4 class="modal-title">Select a cover photo</h4>
        				</div>
        				<div class="modal-body">
      							<img class="thumb" onclick="selectImage(this.id)" src="${pageContext.request.contextPath}/images/covers/Cover.jpg" id="${pageContext.request.contextPath}/images/covers/Cover.jpg" />
								<img class="thumb" onclick="selectImage(this.id)" src="${pageContext.request.contextPath}/images/covers/Cover2.jpg" id="${pageContext.request.contextPath}/images/covers/Cover2.jpg" />
								<img class="thumb" onclick="selectImage(this.id)" src="${pageContext.request.contextPath}/images/covers/Cover3.jpg" id="${pageContext.request.contextPath}/images/covers/Cover3.jpg" />
								<img class="thumb" onclick="selectImage(this.id)" src="${pageContext.request.contextPath}/images/covers/Cover4.jpg" id="${pageContext.request.contextPath}/images/covers/Cover4.jpg" />
								<img class="thumb" onclick="selectImage(this.id)" src="${pageContext.request.contextPath}/images/covers/Cover5.jpg" id="${pageContext.request.contextPath}/images/covers/Cover5.jpg" />
        				</div>
       			 		<div class="modal-footer">
        					<button type="button" class="btn btn-default" data-dismiss="modal">Save</button>
      					</div>
      				</div>
     			</div>
  				</div>

			<div class="modal fade" id="imageBorders" role="dialog" style="z-index:30000">
    			<div class="modal-dialog">
    				<div class="modal-content">
       					 <div class="modal-header">
          						<button type="button" class="close" data-dismiss="modal">&times;</button>
          						<h4 class="modal-title">Select an image border</h4>
        				</div>
        				<div class="modal-body">
          					<img class="thumb" onclick="selectBorder(this.id)" src="${pageContext.request.contextPath}/images/icons/noBorder.jpg" alt="none" id="border:0px solid black"" />	
						<img class="thumb" onclick="selectBorder(this.id)" src="${pageContext.request.contextPath}/images/icons/thinBorder.jpg" alt="none" id="border:5px solid black" />
						<img class="thumb" onclick="selectBorder(this.id)" src="${pageContext.request.contextPath}/images/icons/thickBorder.jpg" alt="none" id="border:10px solid black" />
        				</div>
        				<div class="modal-footer">
        					<button type="button" class="btn btn-default" data-dismiss="modal">Save</button>
      					</div>	
      			</div>
    		</div>
  		</div>
			<%ArrayList<String> cartImages= (ArrayList<String>) (session.getAttribute("MEDIA_CART")); 
					FedoraCommunicator fc= new FedoraCommunicator();
					FedoraDigitalObject ob;%>
					<div id="cart" class="scroll droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)" style="border: 1px solid #aaaaaa; diplay:inline-block; background:#F8F8F8;">
							<%for (int i=0;i<cartImages.size();i++){ 
									if(cartImages.get(i)!=null){
										ob=null;
										ob=fc.populateFedoraDigitalObject(cartImages.get(i));%>
  										<img class="img-circle" src="<%=ob.getDatastreams().get("IMG").getContent() %>"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="<%=cartImages.get(i)%>">
  							<%}} %>	
				
			</div>
		</div>
		
		<div class="col-sm-7">	
  		<div class="slider4">
  			<div class="slide">
				<div class="droptarget droptargetTemplate img-circle" id="0" ondrop="drop(event)" ondragover="allowDrop(event)" >		
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap0" placeholder="Add text to me"></textarea>
				</div>
				
				<div class="droptarget droptargetTemplate img-circle" id="1" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap1" placeholder="Add text to me"></textarea>
				</div>
		
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate img-circle" id="2" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap2" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate img-circle" id="3" ondrop="drop(event)"ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap3" placeholder="Add text to me" ></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate img-circle" id="4" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap4" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate img-circle" id="5" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap5" placeholder="Add text to me"></textarea>
				</div>
			</div>	
			<div class="slide">
				<div class="droptarget droptargetTemplate img-circle" id="6" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap6" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate img-circle" id="7" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap7" placeholder="Add text to me"></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate img-circle" id="8" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap8"placeholder="Add text to me" ></textarea>
				</div>
				<div class="droptarget droptargetTemplate img-circle" id="9" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap9" placeholder="Add text to me"></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate img-circle" id="10" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap10" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate img-circle" id="11" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea maxlength="50" class="TextArea" rows="2" cols="26"  name="input_cap11" placeholder="Add text to me"></textarea>
				</div>
			</div>
		</div>
		</div>
		<div class="col-sm-1">
			 <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#myModal">Next</button>
		</div>
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

</html>

















