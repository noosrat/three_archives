<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>	
<title>Exhibition viewer</title>
<meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  
  <link type="text/css" href="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
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
</head>

<body>
	<div class="container">
	<div class="well">
	
		<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
			<input type="submit" value="Cancel" name="save_exhibition"/>
			<input type="submit" value="Save" name="save_exhibition"/>
		</form>
	
		<h1>Exhibition Title</h1> 
		<p><%out.println(request.getAttribute("ExhibitionTitle")); %></p>
		<h1>Created by</h1> 
			<p><%out.println(request.getAttribute("ExhibitionCreator")); %></p>
		</div>
		<h1>Description</h1> 
			<p><%out.println(request.getAttribute("ExhibitionDescription")); %></p>
	
	</div>
	
	<%String[] images =(String [])session.getAttribute("POPULATED_TEMPLATE_ARRAY"); %>
	<%String[] captions =(String [])session.getAttribute("CAPTIONS"); %>
	<h1>ACTUAL EXHIBITION DISPLAYED</h1>
	
	
		<div class="slider4">
  			<div class="slide">
				<div class="droptarget droptargetTemplate" id="0" style="width:80%; height:250px;">
					<img src="<%=images[0]%>">		
					<textarea rows="1" cols="23" name="input_cap0" readonly=readonly > <%=captions[0] %></textarea>
						
				</div>
				<div class="droptarget droptargetTemplate" id="1" style="width:80%">
					<ul style="list-style-type:none;margin:0;padding:0;">
					<li style="display:inline; float:left;font-weight:bold;">
						<img src="<%=images[1]%>">
					</li>	
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23"  name="input_cap1" readonly=readonly > <%=captions[1] %></textarea>
						</li>
					</ul>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="2" style="width:80%">
					<img src="<%=images[2]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23"  name="input_cap2" readonly=readonly > <%=captions[2] %></textarea>
						</li>
					</ul>
				</div>
				<div class="droptarget droptargetTemplate" id="3"  style="width:80%">
					<img src="<%=images[3]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23"  name="input_cap3"readonly=readonly > <%=captions[3] %></textarea>
						</li>
					</ul>	
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="4"  style="width:80%">
					<img src="<%=images[4]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23"  name="input_cap4"readonly=readonly > <%=captions[4] %></textarea>
						</li>
					</ul>
				</div>
				<div class="droptarget droptargetTemplate" id="5" style="width:80%">
					<img src="<%=images[5]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap5" readonly=readonly > <%=captions[5] %></textarea>
						</li>
					</ul>
				</div>
			</div>	
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="6"  style="width:80%">
					<img src="<%=images[6]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap6" readonly=readonly > <%=captions[6] %></textarea>
						</li>
					</ul>
				</div>
				<div class="droptarget droptargetTemplate" id="7"  style="width:80%">
					<img src="<%=images[7]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap7" readonly=readonly > <%=captions[7] %></textarea>
						</li>
					</ul>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="8"  style="width:80%">
					<img src="<%=images[8]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap8" readonly=readonly > <%=captions[8] %></textarea>
						</li>
					</ul>
				</div>
				<div class="droptarget droptargetTemplate" id="9" style="width:80%">
					<img src="<%=images[9]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap9" readonly=readonly > <%=captions[9] %></textarea>
						</li>
					</ul>
				</div>
			</div>
			<div class="slide">
				<div class="droptarget droptargetTemplate" id="10" style="width:80%">
					<img src="<%=images[10]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap10" readonly=readonly > <%=captions[10] %></textarea>
						</li>
					</ul>
				</div>
				<div class="droptarget droptargetTemplate" id="11" style="width:80%">
					<img src="<%=images[11]%>">
					<ul style="list-style-type:none;margin:0;padding:0;">
						<li style="display:inline; float:left;font-weight:bold;">	
							<textarea rows="1" cols="23" name="input_cap11" readonly=readonly > <%=captions[11] %></textarea>
						</li>
					</ul>
				</div>
			</div>
		</div>
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	</div>
</body>
</html>

