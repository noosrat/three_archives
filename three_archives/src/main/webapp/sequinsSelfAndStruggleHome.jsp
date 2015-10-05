<!DOCTYPE html>
<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="search.SearchAndBrowseCategory"%>
<%@page import="common.fedora.DatastreamID"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>

<head>

<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">

<title>Personal Histories - Centre for Curating The Archive</title>

<!-- Bootstrap Core CSS -->
<link
	href="${pageContext.request.contextPath}/bootstrap-3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/bootstrap-3.3.5/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/typeahead.js"></script>
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

<script type="text/javascript">
	$(document).ready(function() {
		var words = "/data/sequins.json";
		var countries = new Bloodhound({
			datumTokenizer : Bloodhound.tokenizers.whitespace,
			queryTokenizer : Bloodhound.tokenizers.whitespace,
			limit : 5,
			prefetch : {
				url : words,
			}
		});
		$('#prefetch .typeahead').typeahead(null, {
			name : 'countries',
			source : countries
		});
	});
</script>
<style>
	 
  .carousel-inner > .item > img,
  .carousel-inner > .item > a > img {
      margin: auto;
  }

	
		.Sequins{
			width:150px;
			height:100px;
			background-color: #ffffff;
    			border: 1px solid black;
    			opacity: 0.8;	

		}
		h3{
			margin: 5%;
    			font-weight: bold;
    			color: #000000;
		}
</style>
</head>

<body>

	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top navbar-left"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}/archives/SequinsSelfAndStruggle">Sequins, Self and Struggle</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<li><a
						href="${pageContext.request.contextPath}/archives/browse">Browse</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_exhibitions">Exhibitions</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_maps">Maps</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_uploads">Uploads</a></li>
					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_downloads">Downloads</a></li>
				</ul>
				<!-- search components-->
				<div id="bs-example-navbar-collapse-1"
					class="collapse navbar-collapse">

					<ul class="nav navbar-nav navbar-right">
						<li class="dropdown"><a
							href="${pageContext.request.contextPath}/archives/search_objects"
							class="dropdown-toggle" data-toggle="dropdown" role="button"
							aria-haspopup="true" aria-expalinded="false">${searchCategories[0]}<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:forEach var="searchCategory" items="${searchCategories}">
									<li><a
										href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>

								</c:forEach>
							</ul></li>
						<li>
							<form class="navbar-form navbar-right"
								action="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategories[0]}"
								method="post">
								<div class="form-group">
									<div id="prefetch">
										<input
											class="form-control typeahead tt-query tt-hint tt-dropdown-menu tt-suggestion"
											data-provider="typeahead" type="text"
											placeholder="Search Archive" autocomplete="off"
											spellcheck="false" name="terms">
									</div>
								</div>

								<button type="submit" class="btn">Search</button>

								<div class="checkbox">
									<label> <input type="checkbox" id="limitSearch"
										name="limitSearch" value="limitSearch"><font
										color="white"> Limit search to these results</font>
									</label>
								</div>

							</form>
						</li>
					</ul>


				</div>
			</div>
			<!-- end of search bar components -->

		</div>

	</nav>


	<div class="container">
	<p></p><br><br><br>
		<div class="row">
			<div class="col-lg-12" style="text-align:center">
				<h2>About Sequins, Self & Struggle</h2>
				<div class="well well-lg" style="background-color:	#FFF0F0"> <p>This project is a collaboration among the Departments of Drama at Royal Holloway and Queen Mary, University of London (UK), The Centre for Curating the Archive and the Centre for African Studies at the University of Cape Town, Africana Studies at Brown University (US) and the District 6 Museum. The primary aims are to research, document and disseminate archives of the Spring Queen and Miss Gay Western Cape pageants performed by disparate 'coloured' communities in greater Cape Town.</p>

				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12" style="text-align:center">
				 <div id="myCarousel" class="carousel slide" data-ride="carousel">
    <!-- Indicators -->
    <ol class="carousel-indicators">
      <li data-target="#myCarousel" data-slide-to="0" class="active"></li>
      <li data-target="#myCarousel" data-slide-to="1"></li>
     
    </ol>

    <!-- Wrapper for slides -->
    <div class="carousel-inner" role="listbox">

      <div class="item active">
		<div class="row">
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/10.jpg" width="200">
			</div>
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/15.jpg" width="200">
			</div>
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/2.jpg" width="200">
			</div>
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/3.jpg" width="200">
			</div>
		</div>
 
      </div>

      <div class="item">
        <div class="row">
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/5.jpg" width="200">
			</div>
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/6.jpg" width="200">
			</div>
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/7.jpg" width="200">
			</div>
			<div class="col-lg-3" style="text-align:center">
        			<img src="${pageContext.request.contextPath}/images/8.jpg" width="200">
			</div>
		</div>
  
      </div>
    
     
  
    </div>

    <!-- Left and right controls -->
    <a class="left carousel-control" href="#myCarousel" role="button" data-slide="prev">
      <span class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
      <span class="sr-only">Previous</span>
    </a>
    <a class="right carousel-control" href="#myCarousel" role="button" data-slide="next">
      <span class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
      <span class="sr-only">Next</span>
    </a>
  </div>
			</div>
		</div>
		<!-- /.row -->
	</div>
	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid"
		role="navigation">
		<div class="container">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand" href="${pageContext.request.contextPath}">Personal
					Histories</a>
			</div>
		</div>
		<!-- /.container -->
	</nav>
	<!-- /.container -->


	<!-- Script to Activate the Carousel -->
	<script>
		$('.carousel').carousel({
			interval : 5000
		//changes the speed
		})
	</script>

</body>

</html>