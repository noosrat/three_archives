<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="search.SearchAndBrowseCategory"%>
<%@page import="common.fedora.DatastreamID"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Three Archives : Search and Browse</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/search_and_browse.css"></link>
		<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet">
	<link href="${pageContext.request.contextPath}/css/lightbox.css" rel="stylesheet">



<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/typeahead.js"></script>
	<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/lightbox.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var archive = '<%= session.getAttribute("ARCHIVE") %>';
	if (archive == "Sequins, Self and Struggle") {
		var words = "/data/sequins.json";
	} else if (archive == "Movie Snaps") {
		var words = "/data/movie.json";
	} else if (archive == "Harfield Village") {
		var words = "/data/harfield.json";
	}

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



</head>


<body>

	<nav class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand"
					href="${pageContext.request.contextPath}/archives/browse">${ARCHIVE}</a> <a class="navbar-brand"> ${message}</a>

			</div>

			<div id="navbar" class="navbar-collapse collapse">
				<c:forEach var="category" items="${browseCategories}">
					<!--    Category: ${category.key}  - Value: ${category.value} <br> -->

					<ul class="nav navbar-nav">
						<li class="dropdown"><a
							href="${pageContext.request.contextPath}/archives/browse?category=${category.key}"
							class="dropdown-toggle" data-toggle="dropdown" role="button"
							aria-haspopup="true" aria-expanded="false">${category.key}<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:forEach var="categoryValue" items="${category.value}">
									<li><a
										href="${pageContext.request.contextPath}/archives/browse?category=${category.key}&${category.key}=${categoryValue}">${categoryValue}</a></li>
								</c:forEach>
							</ul>
					</ul>
				</c:forEach>
				<!--/.nav-collapse -->
			</div>
			<div id="navbar" class="collapse navbar-collapse">

				<form class="navbar-form navbar-right"
					action="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategories[0]}"
					method="post">


					<div class="form-group">
						
						
						
<div id="prefetch">
  <input class="form-control typeahead tt-query" data-provider="typeahead" type="text" placeholder="Search Archive" autocomplete="off" spellcheck="false" name="terms">
</div>
</div>
					<!--    Category: ${category.key}  - Value: ${category.value} <br> -->
					<ul class="nav navbar-nav">
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
							</ul>
					</ul>
					<button type="submit" class="btn btn-success">Search</button>
					<br>

					<div class="checkbox">
						<label> <input type="checkbox" id="limitSearch"
							name="limitSearch" value="limitSearch">Limit search to
							these results
						</label>
					</div>

				</form>


			</div>
		</div>

		<!--/.nav-collapse -->
		</div>
	</nav>

	<br>
	<br>
	<br>
	<br>




	<br>
	<br>
	<br>
	<br>
	<!--  portfolio grid section displaying the images -->
	<section id="portfolio">
		<div class="container">
			<!-- <div class="row">-->
			<!--  for loop going through the digital objects -->
			<c:set var="count" value="0" scope="page" />
			<c:forEach var="digitalObject" items="${objects}">



				<div class="col-sm-4 portfolio-item">
					<a href="#portfolioModal${count}" class="portfolio-link"
						data-toggle="modal"> <!-- <td>${digitalObject}</td>--> <!--  for loop going through the digital objects' datastreams -->

						<div class="caption">
							<div class="caption-content">${digitalObject.datastreams['DC'].dublinCoreMetadata}</div>
							<!-- caption content -->
						</div> <!-- caption -->
						
						
						 <img
						src="${digitalObject.datastreams['IMG'].content}"
						class="img-thumbnail img-responsive" alt="image unavailable">
					</a>
				</div>
				<!-- "col-sm-4 portfolio-item" -->
				<c:set var="count" value="${count + 1}" scope="page" />
			</c:forEach>
			<!-- </div>-->
			<!--  row -->
		</div>
		<!--  container -->
	</section>

	<!-- portfolio modal section containing results to the above links -->


	<c:set var="count" value="0" scope="page" />
	<c:forEach var="digitalObject" items="${objects}">
		<div class="portfolio-modal modal fade" id="portfolioModal${count}"
			tabindex="-1" role="dialog" aria-hidden="false">
			<div class="modal-content" data-dismiss="modal">
				<div class="close-modal" data-dismiss="modal">
					<div class="lr">
						<div class="rl"></div>
					</div>
				</div>
				<div class="container">
					<div class="row">
						<div class="col-lg-8 col-lg-offset-2">
							<div class="modal-body">


								<img src="${digitalObject.datastreams['IMG'].content}"
									class="img-thumbnail img-responsive" alt="image unavailable">
								<br> <br>
								<div class="item-details">${digitalObject.datastreams['DC'].dublinCoreMetadata}
								</div>
								<!-- item details -->


								<br>
								<!-- 	<button type="button" class="btn btn-default"
									>
									<i class="fa fa-times"></i> Place me
								</button> -->
							</div>
							<!-- modal body -->
						</div>
						<!--  col-lg-8 -->
					</div>
					<!-- row -->
				</div>
				<!-- container -->
			</div>
			<!-- modal-content -->
		</div>
		<!--  portfolio-modal modal fade -->
		<c:set var="count" value="${count + 1}" scope="page" />
	</c:forEach>




</body>
</html>