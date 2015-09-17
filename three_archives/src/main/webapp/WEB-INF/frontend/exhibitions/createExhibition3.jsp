<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>


<div class="container">
  <h2>Enter exhibition details</h2>
  <form role="form" method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">

    <div class="form-group">
      <label for="usr">Title</label>
      <input type="text" name="Title" class="form-control">
    </div>

	 <div class="form-group">
      		<label for="usr">Description</label>
      		<input type="text" name="Description" class="form-control">
    	</div>

 <div class="form-group">
      <label for="usr">Creator</label>
      <input type="text" name="Creator" class="form-control">
    </div>
	<label>Make exhibition publicly viewable</label><br>
	<label class="radio-inline">
      		<input type="radio" name="viewable" value="No" >No
    	</label>
    	<label class="radio-inline">
      		<input type="radio" name="viewable" value="yes">Yes
    	</label>
<br><br>
<input type="submit" name="submit_exhibition" value="Back"/> <input type="submit" name="submit_exhibition" value="Next"/>
   
  </form>
</div>


</body>

</html>