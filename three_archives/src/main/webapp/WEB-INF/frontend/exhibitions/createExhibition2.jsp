<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ page import="java.util.ArrayList" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<link type="text/css" href="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
	<script src="${pageContext.request.contextPath}/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.min.js"></script>

	<script>
		$(document).ready(function(){
 		 $('.slider4').bxSlider({
   		 slideWidth: 250,
   		 minSlides: 2,
   	 	 maxSlides: 3,
   		 moveSlides: 2,
   	 	slideMargin: 8,
   	 	infiniteLoop:false,
		hideControlOnEnd:true
  		});
		});
	</script>
	
	<title>Exhibition viewer</title>

	<style>
		.droptargetCart {
 			margin: 5px;
    		padding: 5px;
   		}
		.droptargetTemplate {
   			width: 100px; 
    		height: 200px;
    		margin: 15px;
    		padding: 10px;
    		border: 1px dotted #aaaaaa;
    	}
		.nodroptargetTemplate {
    		width: 100px; 
    		height: 200px;
    		margin: 15px;
    		padding: 10px;
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
    	var data = event.dataTransfer.getData("Text");
    	event.target.appendChild(document.getElementById(data));
    	//document.getElementById("demo").innerHTML = document.getElementById(data);
    	//document.getElementById("demo").innerHTML = document.getElementById("demo").value + event.target.id + " " + data + " " ;

		//if(document.getElementById(data).style.width=="50%"){
			//document.getElementById(data).style.width="100%";
		//}
		//else{
			//document.getElementById(data).style.width="50%";
		//}
		
		if(document.getElementById(data).style.width=="50%")
		{
			document.getElementById(data).style.width="100%";
			document.getElementById("demo").innerHTML = document.getElementById("demo").value + event.target.id + " " + data + " " ;
				
		}
		
		else if (event.target.id=="cart")
		{
			document.getElementById(data).style.width="50%";
			document.getElementById("demo").innerHTML = document.getElementById("demo").value + event.target.id + " " + data + " " ;
		}
		else if (document.getElementById(data).style.width=="100%")
		{
			document.getElementById("demo").innerHTML = document.getElementById("demo").value + "REMOVE " + data + " " + event.target.id + " " + data + " ";
				
		}
	}
</script>
</head>
<body>
	<header>
		<h1>Create Exhibition</h1>
	</header>
	
	
	
	<br>
	<ul id="mainEx" style="list-style-type:none;margin:0;padding:0;">
		<li style="display:inline;float:left;font-weight:bold;">
		
			<div style="border: 1px solid #aaaaaa;width: 200px; diplay:inline-block; background:#F8F8F8;">
				<h1>My image shelf</h1>
				<div class="droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)">
  					<img  src="${pageContext.request.contextPath}/images/1.jpg"  style="width:50%;" ondragstart="dragStart(event)" draggable="true" id="image1">
				</div>
				<div class="droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)">
  					<img  src="${pageContext.request.contextPath}/images/2.jpg"  style="width:50%;" ondragstart="dragStart(event)" draggable="true" id="image2">
				</div>
				<div class="droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)">
  					<img  src="${pageContext.request.contextPath}/images/3.jpg"  style="width:50%;" ondragstart="dragStart(event)" draggable="true" id="image3">
				</div>
				<div class="droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)">
  					<img  src="${pageContext.request.contextPath}/images/4.jpg"  style="width:50%;" ondragstart="dragStart(event)" draggable="true" id="image4">
				</div>
				<div class="droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)">
  					<img  src="${pageContext.request.contextPath}/images/5.jpg"  style="width:50%;" ondragstart="dragStart(event)" draggable="true" id="image5">
				</div>
			</div>
		</li>
		
		<%if (request.getAttribute("returnedExhibitionTemplate").equals("1")){%>
		<li style="display:inline; float:left;font-weight:bold;margin:80px;">
		
		<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
			<textarea id="demo" name="user_action" readonly=readonly style="display:none;"> </textarea>
			<input type="submit" value="Back" name="exhibition_det"/>
			<input type="submit" value="Next" name="exhibition_det"/>
		</form>
		
  		<div class="slider4">
  			<div class="slide">
				<div class="droptarget droptargetTemplate" id="0" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="1" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="2" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="3" ondrop="drop(event)"ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="4" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="5" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>	
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="6" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="7" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="8" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="9" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="10" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="11" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
		</div>
		</li>
	<%} %>

	<%if (request.getAttribute("returnedExhibitionTemplate").equals("2")){%>
	<li style="display:inline; float:left;font-weight:bold;margin:80px;">
		
		<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
			<textarea id="demo" name="user_action" readonly=readonly style="display:none;"> </textarea>
			<input type="submit" value="Back" name="exhibition_det"/>
			<input type="submit" value="Next" name="exhibition_det"/>
		</form>
		
		<div class="slider4">
			<div class="slide">
					<div class="droptarget droptargetTemplate" id="0" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
					<div class="droptarget nodroptargetTemplate" id="1" style="width:80%"></div>
			</div>
			<div class="slide">
					<div class="droptarget nodroptargetTemplate" id="2" style="width:80%"></div>
					<div class="droptarget droptargetTemplate" id="3" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="4" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget nodroptargetTemplate" id="5" style="width:80%"></div>
			</div>	
			<div class="slide">
				<div class="droptarget nodroptargetTemplate" id="6"  style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="7" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="8" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
				<div class="droptarget nodroptargetTemplate" id="9"  style="width:80%"></div>
			</div>
			<div class="slide">
				<div class="droptarget nodroptargetTemplate" id="10" style="width:80%"></div>
				<div class="droptarget droptargetTemplate" id="11" ondrop="drop(event)" ondragover="allowDrop(event)" style="width:80%"></div>
			</div>
		</div>
		</li>
		<%}%>
	</ul>

</body>