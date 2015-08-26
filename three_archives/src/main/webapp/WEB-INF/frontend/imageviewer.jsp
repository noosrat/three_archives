<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% String pic = request.getParameter("image"); %>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie Snaps | Image View</title>
</head>
<body>
<h1>Name</h1>
<img src="<%= pic %>">
<h4>Details</h4>
</body>
</html>