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
 		 $('.slider').bxSlider({
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
 <h1>Title: <%out.println(request.getAttribute("returnedExhibitionTitle")); %></h1>
</header>
<h1>Description</h1>
<p><%out.println(request.getAttribute("returnedExhibitionDescription")); %></p>

<div class="slider"  >
  <div class="slide"><img src="${pageContext.request.contextPath}/images/1.jpg" > <img src="${pageContext.request.contextPath}/images/2.jpg"></div>
   <div class="slide"><img src="${pageContext.request.contextPath}/images/2.jpg"><img src="${pageContext.request.contextPath}/images/6.jpg"></div>
   
     <div class="slide"><img src="${pageContext.request.contextPath}/images/3.jpg"> <img src="${pageContext.request.contextPath}/images/7.jpg"></div>
       <div class="slide"><img src="${pageContext.request.contextPath}/images/4.jpg"><img src="${pageContext.request.contextPath}/images/8.jpg"></div>
         <div class="slide"><img src="${pageContext.request.contextPath}/images/5.jpg"><img src="${pageContext.request.contextPath}/images/9.jpg"></div>
</div>
     
<div>
	<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
	<br>
	<br>
	<h1>Allow exhibition to be publicly viewed *check box*</h1>
	<input type="submit" value="Save"/>
	<input type="submit" value="Back"/>

	
</form>
</div>


</body>
</html>
