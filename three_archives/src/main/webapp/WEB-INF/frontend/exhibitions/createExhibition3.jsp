<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<h1>Actions: <%out.println(request.getAttribute("user_actions")); %></h1>
<h1>Template: <%out.println(request.getAttribute("template")); %></h1>
<br>
<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
	
	Title: <input type="text" name="Title"> <br>
	Description: <input type="text" name="Description"> <br>
	Creator: <input type="text" name="Creator"> <br>
	Date Created: <input type="text" name="Date"><br><br>
	Make exhibition publicly viewable: <br>
	<input type="radio" name="viewable" value="yes" checked> Yes
	<br>
	<input type="radio" name="viewable" value="No" checked> No
	<br><br>
	<input type="submit" name="submit_exhibition" value="Back"/> <input type="submit" name="submit_exhibition" value="Next"/>	
</form>


</body>

</html>