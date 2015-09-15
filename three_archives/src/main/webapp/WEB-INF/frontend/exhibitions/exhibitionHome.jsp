<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<link href="text/css" href="${pageContext.request.contextPath}/WEB-INF/frontend/exhibitions/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
</head>
<header>
	<h1>Exhibition Home</h1>
</header>
<form method="post" action="${pageContext.request.contextPath}/archives/view_exhibitions">

	
	<header>
		<input type="submit" name="view_all_exhibitions" value="View All Exhibitions"/>	
		<input type="submit" name="create_exhibition" value="Create Exhibition"/>	
	</header>
</form>

<div>

</div>