<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Personal Histories - Centre for Curating The Archive</title>

    <!-- Bootstrap Core CSS -->
    <link href="${pageContext.request.contextPath}/bootstrap-3.3.5/css/bootstrap.min.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/css/full-slider.css" rel="stylesheet">

    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>

<body>

    <!-- Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
        <div class="container">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
                <a class="navbar-brand" href="#">Personal Histories</a>
            </div>
        </div>
        <!-- /.container -->
    </nav>

    <!-- Full Page Image Background Carousel Header -->
    <header id="myCarousel" class="carousel slide">
        <!-- Indicators -->
        <ol class="carousel-indicators"> <!-- CHANGE DYNAMICALLY GENERATE DEPENDENT ON ARCHIVES WE HAVE --><li data-target="#myCarousel" data-slide-to="0" class="active"></li><li data-target="#myCarousel" data-slide-to="1"></li><li data-target="#myCarousel" data-slide-to="2"></li>
        </ol>
	 
        <!-- Wrapper for Slides -->
        <div class="carousel-inner"><div class="item active"><div class="fill" style="background-image:url(${pageContext.request.contextPath}/images/spring.jpg);"></div><div class="carousel-caption"><h2>Sequins, Self And Struggle </h2><p><a class="btn btn-large btn-primary" href="${pageContext.request.contextPath}/archives/SequinsSelfAndStruggle">Explore Archive</a></p></div></div><div class="item"><div class="fill" style="background-image:url(${pageContext.request.contextPath}/images/movie.jpg);"></div><div class="carousel-caption"><h2>Movie Snaps</h2><p><a class="btn btn-large btn-primary" href="${pageContext.request.contextPath}/archives/MovieSnaps">Explore Archive</a></p></div></div>
        <div class="item"><div class="fill" style="background-image:url(${pageContext.request.contextPath}/images/harfield.jpg);"></div><div class="carousel-caption"><h2>Harfield Village	</h2><p><a class="btn btn-large btn-primary" href="${pageContext.request.contextPath}/archives/HarfieldVillage">Explore Archive</a></p></div></div>
        </div>

        <!-- Controls -->
        <a class="left carousel-control" href="#myCarousel" data-slide="prev">
            <span class="icon-prev"></span>
        </a>
        <a class="right carousel-control" href="#myCarousel" data-slide="next">
            <span class="icon-next"></span>
        </a>
	<div>
	<p></p><br><br><br>	
		</div>
    </header>
	<div class="modal fade" id="login" role="dialog" style="z-index:30000">
	<form role="form" method="post" action="${pageContext.request.contextPath}/archives/auth_user">
    			<div class="modal-dialog">
    				<div class="modal-content">
       					 <div class="modal-header">
          						<button type="button" class="close" data-dismiss="modal">&times;</button>
          						<h4 class="modal-title">Enter details</h4>
        				</div>
        				<div class="modal-body">
        				
          					<div class="form-group">
    							<label for="username">User name:</label>
    								<input class="form-control" id="username" name="new_username">
  							</div>
  							<div class="form-group">
    							<label for="pwd">Password:</label>
    							<input type="password" class="form-control" id="pwd" name="new_pwd">
  							</div>
        				
        				</div>
					<div class="modal-footer">
        					<input type="submit" value="Submit" class="btn btn-primary btn-sm" name="authorise"/>
      					</div>
       			 		
      				</div>
     
    			</div>
    			</form>
    			
  		</div>

	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">

			<div class="row" >
				<div class="navbar-header col-sm-8">
					<a class="navbar-brand" href="${pageContext.request.contextPath}"><span class="glyphicon glyphicon-home"></span> Personal Histories</a>
				</div>
			<%if (session.getAttribute("USER")==null){%>
				<div class="col-sm-3">
				</div>
				<div class="col-sm-1">
                			<a style="margin:15px" data-toggle="modal" data-target="#login" class="navbar-brand" href="#login">Login</a>
				</div>
			<%}
			 else if (session.getAttribute("USER").equals("incorrect")){%>
				<div class="col-sm-3">
					<div class="navbar-brand">Credentials incorrect</div>
				</div>
				<div class="col-sm-1">
					<a style="margin:15px" data-toggle="modal" data-target="#login" class="navbar-brand" href="#login">Login</a>
				</div>
                	<%}
			else if (session.getAttribute("USER").equals("false")){%>
				<div class="col-sm-3">
				</div>
				<div class="col-sm-1">
					<a style="margin:15px" data-toggle="modal" data-target="#login" class="navbar-brand" href="#login">Login</a>
				</div>
			<%} 
			 else if (session.getAttribute("USER").equals("ADMINISTRATOR")){%>
				<div class="col-sm-3">
					<div class="navbar-brand">Logged on as Administrator</div>
				</div>
				<div class="col-sm-1">
					<form role="form" method="post" action="${pageContext.request.contextPath}/archives/logout_user">
                				<input style="margin:15px" type="submit" value="logout" class="btn btn-primary btn-xs" name="logout"/>
                			</form>
				</div>
			<%}
			else if(session.getAttribute("USER").equals("privileged")){%>
				<div class="col-sm-3">
					<div style="margin-left:600px;" class="navbar-brand">Logged on as a privileged user</div>
				</div>
				<div class="col-sm-1">
					<form role="form" method="post" action="${pageContext.request.contextPath}/archives/logout_user">
                				<input style="margin:15px" type="submit" value="logout" class="btn btn-primary btn-xs" name="logout"/>
                			</form>
				</div>

			<%} %>
			</div>       	
                
            </div>
        </div>
        <!-- /.container -->
    </nav>
    <!-- /.container -->

    <!-- jQuery -->
    <script src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>

    <!-- Bootstrap Core JavaScript -->
    <script src="${pageContext.request.contextPath}/bootstrap-3.3.5/js/bootstrap.min.js"></script>

    <!-- Script to Activate the Carousel -->
    <script>
    $('.carousel').carousel({
        interval: 3000 //changes the speed
    })
    </script>

</body>

</html>