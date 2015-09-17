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
</head>

<body>
	<div class="container">
	<div class="well">
		<h1>Exhibition Title</h1> 
		<p><%out.println(request.getAttribute("ExhibitionTitle")); %></p>
		<h1>Created by</h1> 
			<p><%out.println(request.getAttribute("ExhibitionCreator")); %></p>
		</div>
		<h1>Description</h1> 
			<p><%out.println(request.getAttribute("ExhibitionDescription")); %></p>
	
		</div>
	</div>
</body>
</html>

