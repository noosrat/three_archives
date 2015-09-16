<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

  <title>Archive</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

</head>
<body>

<%String archive=(String) session.getAttribute("ARCHIVE");%>
<div class="container">
  <div class="header">
	<%if (archive.equals("Sequins, Self and Struggle")){ %>
    		<h1>Sequins, Self and Struggle</h1> 
	<%}
	else if (archive.equals("Movie Snaps")){%>
		<h1>Movie Snaps</h1> 
	<%}
	else if (archive.equals("Harfield Village")){%>
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



${message}
<form
		action="${pageContext.request.contextPath}/archives/redirect_search"
		method="post">

		<input type="submit" value="Search" />

	</form>
	<form
		action="${pageContext.request.contextPath}/archives/redirect_exhibitions"
		method="post">


		<input type="submit" value="Exhibitions" />

	</form>
	<form
		action="${pageContext.request.contextPath}/archives/redirect_maps"
		method="post">
		<input type="submit" value="Maps" />
	</form>

</div>



</body>
</html>