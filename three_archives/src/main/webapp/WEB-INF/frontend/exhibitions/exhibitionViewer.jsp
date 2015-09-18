<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link type="text/css" href="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
<script src="${pageContext.request.contextPath}/jquery.min.js"></script>
<script src="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.min.js"></script>
	
	<script>
		$(document).ready(function(){
 		 $('.slider4').bxSlider({
   		 slideWidth: 300,
   		 minSlides: 2,
   	 	 maxSlides: 4,
   		 moveSlides: 2,
   	 	slideMargin: 8
  		});
		});
	</script>
	
<title>Exhibition viewer</title>

</head>

<body>
<header>
	<h1>Exhibition Titlearoo: </h1> <h1><%out.println(request.getAttribute("message")); %></h1>
	<h1>Exhibition Title using object: </h1> <h1><%out.println(request.getAttribute("message")); %></h1>
</header>

<%String [] imageTitles= (String[]) (request.getAttribute("images")); %>



<div class="slider4"  >
  <div class="slide"><img src="http://localhost:8080/fedora/objects/sq:15/datastreams/IMG/content" > <img src="<%=imageTitles[1]%>"></div>
   <div class="slide"><img src="<%=imageTitles[2]%>"><img src="<%=imageTitles[3]%>"></div>
   
     <div class="slide"><img src="<%=imageTitles[4]%>"> <img src="<%=imageTitles[5]%>"></div>
       <div class="slide"><img src="<%=imageTitles[6]%>"><img src="<%=imageTitles[7]%>"></div>
         <div class="slide"><img src="<%=imageTitles[8]%>"><img src="<%=imageTitles[9]%>"></div>
</div>
     



</body>
</html>

