<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>Template 1</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  
  <link type="text/css" href="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
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
  		});
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
<div class="container-fluid">
  <div class="header">
	<%if (session.getAttribute("ARCHIVE").equals("sequins")){ %>
    		<h1>Sequins, Self and Struggle</h1> 
	<%}
	else if (session.getAttribute("ARCHIVE").equals("snaps")){%>
		<h1>Movie Snaps</h1> 
	<%}
	else if (session.getAttribute("ARCHIVE").equals("harfield")){%>
		<h1>Harfield Village</h1> 
	<%}%>
     <a href="#" class="btn btn-info btn-lg"><span class="glyphicon glyphicon-search"></span> Search</a>     
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


<%@ page import="common.fedora.FedoraException"%>
<%@ page import="common.fedora.FedoraClient"%>
<%@ page import="common.fedora.DublinCore"%>
<%@ page import="common.fedora.FedoraDigitalObject"%>
<%@ page import="search.FedoraCommunicator"%>
<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
<ul id="mainEx" style="list-style-type:none;margin:0;padding:0;">
		<li style="display:inline;float:left;font-weight:bold;">
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
		</li>
		<li style="display:inline; float:left;margin-left:50px;">
		
			
  		<div class="slider4">
  			<div class="slide">
				<div class="droptarget droptargetTemplate" id="0" ondrop="drop(event)" ondragover="allowDrop(event)" >		
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap0" placeholder="Add text to me"></textarea>
				</div>
				
				<div class="droptarget droptargetTemplate" id="1" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap1" placeholder="Add text to me"></textarea>
				</div>
		
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="2" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap2" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate" id="3" ondrop="drop(event)"ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap3" placeholder="Add text to me" ></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="4" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap4" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate" id="5" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap5" placeholder="Add text to me"></textarea>
				</div>
			</div>	
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="6" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap6" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate" id="7" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap7" placeholder="Add text to me"></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="8" ondrop="drop(event)" ondragover="allowDrop(event)" >
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap8"placeholder="Add text to me" ></textarea>
				</div>
				<div class="droptarget droptargetTemplate" id="9" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap9" placeholder="Add text to me"></textarea>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="10" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap10" placeholder="Add text to me"></textarea>
				</div>
				<div class="droptarget droptargetTemplate" id="11" ondrop="drop(event)" ondragover="allowDrop(event)">
				</div>
				<div class="caption">
					<textarea class="TextArea" rows="2" cols="26"  name="input_cap11" placeholder="Add text to me"></textarea>
				</div>
			</div>
		</div>
		
	
		</li>
		<li style="display:inline; float:left;margin-left:50px;">
			<textarea id="demo" name="user_action" readonly=readonly style="display:none;"></textarea>
			<input type="submit" value="Back" name="exhibition_det"/>
			 <button type="button" class="btn btn-info btn-lg" data-toggle="modal" data-target="#myModal">Next</button>
			
			<div class="modal fade" id="myModal" role="dialog">
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
								<label>Make exhibition publicly viewable</label><br>
									<label class="radio-inline">
      								<input type="radio" name="viewable" value="No" >No
    							</label>
    						<label class="radio-inline">
      							<input type="radio" name="viewable" value="yes">Yes
    						</label>

        				</div>
        				<div class="modal-footer">
          					<input type="submit" value="Save" name="exhibition_det"/>
         
       			 		</div>
       			 		
      			</div>
     
    		</div>
  </div>
					
			
		
		</li>
	</ul>

</form>
</div>
</body>
</html>