<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>

</head>
<body>
<header>
	<h1>Select an exhibition</h1>
</header>
<%@ page import="common.model.Exhibition"%>
<%Exhibition[] exhibitions= ((Exhibition[])request.getAttribute("all_exhibitions")); %>
<%int numberofExhibitions= ((Exhibition[])request.getAttribute("all_exhibitions")).length;%>



<form method="post" action="${pageContext.request.contextPath}/archives/view_exhibitions">
	<h1>Exhibition:</h1><br>
	<%int i; %>
	<% for (i=1; i<=numberofExhibitions;i++){ %>
	<input type="radio" name="selectedExhibit" value=<%=i%> > Exhibit <%out.println(i);%></input> <br>
	 
	<br>
	<%} %>
	<input type="submit" value="View Exhibition"/>	
</form>

<div>

</div>

</body>
</html>