<html>
<head>
  <title>Archive</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
</head>
<body>


	<div class="container">
  		<div class="jumbotron">
    		<h1>Personal Histories</h1>      
  		</div>  
  		
  	<div class="row">
		${message}
    	<div class="col-sm-4" style= "border: 1px solid; ">
		
			<form
				action="${pageContext.request.contextPath}/archives/redirect_sequins"
				method="post">
				<input type="submit" value="sequins" />
			</form>
			
    	</div>

    	<div class="col-sm-4" style= "border: 1px solid; ">
			<form
				action="${pageContext.request.contextPath}/archives/redirect_snaps"
				method="post">
				<input type="submit" value="snaps" />
			</form>
	
    	</div>
    	
   	 	<div class="col-sm-4" style= "border: 1px solid; ">
			<form
			action="${pageContext.request.contextPath}/archives/redirect_harfield"
			method="post">

			<input type="submit" value="harfield" />

			</form>
      		
   		</div>
   	
  	</div>

</div>
	
	
</html>
