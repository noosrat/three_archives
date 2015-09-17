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
    	
    	div.scroll{
    		background-color: #00FFFF;
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
</script>
</head>
<body>
<div class="container">
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




<ul id="mainEx" style="list-style-type:none;margin:0;padding:0;">
		<li style="display:inline;float:left;font-weight:bold;">
		
			<div id="cart" class="scroll droptarget droptargetCart" ondrop="drop(event)" ondragover="allowDrop(event)" style="border: 1px solid #aaaaaa; diplay:inline-block; background:#F8F8F8;">
				<h1>My image shelf</h1>
				
  					<img  src="${pageContext.request.contextPath}/images/1.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="1.jpg">
  					<img  src="${pageContext.request.contextPath}/images/2.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="2.jpg">
  					<img  src="${pageContext.request.contextPath}/images/3.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="3.jpg">
  					<img  src="${pageContext.request.contextPath}/images/4.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="4.jpg">
  					<img  src="${pageContext.request.contextPath}/images/5.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="5.jpg">
  					<img  src="${pageContext.request.contextPath}/images/6.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="6.jpg">
  					<img  src="${pageContext.request.contextPath}/images/7.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="7.jpg">
  					<img  src="${pageContext.request.contextPath}/images/8.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="8.jpg">
  					<img  src="${pageContext.request.contextPath}/images/9.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="9.jpg">
  					<img  src="${pageContext.request.contextPath}/images/10.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="10.jpg">
  					<img  src="${pageContext.request.contextPath}/images/11.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="11.jpg">
  					<img  src="${pageContext.request.contextPath}/images/12.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="12.jpg">
  					<img  src="${pageContext.request.contextPath}/images/13.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="13.jpg">
  					<img  src="${pageContext.request.contextPath}/images/14.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="14.jpg">
  					<img  src="${pageContext.request.contextPath}/images/15.jpg"  style="width:40%;" ondragstart="dragStart(event)" draggable="true" id="15.jpg">
  				
			</div>
		</li>
		<li>
		</li>
		
</ul>


</div>
</body>
</html>