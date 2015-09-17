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
<script type="text/javascript">
	$(document).ready(
			function() {
				$('input.typeahead').typeahead(
						{
							name : 'accounts',
							local : [ 'Audi', 'BMW', 'Bugatti', 'Ferrari',
									'Ford', 'Lamborghini', 'Mercedes Benz',
									'Porsche', 'Rolls-Royce', 'Volkswagen' ]
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
				<a class="navbar-brand" href="#">Harfield Village</a> <a
					class="navbar-brand"> ${message}</a>
				<!-- <a class="navbar-brand" href="#">Sequins, Self and Struggle</a>
				<a class="navbar-brand" href="#">Movie Snaps</a>-->

			</div>

			<div id="navbar" class="navbar-collapse collapse">
				<c:forEach var="category" items="${categories}">
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
					action="${pageContext.request.contextPath}/archives/search_objects"
					method="post">
					<div class="form-group">
						<input type="text" placeholder="Search archive"
							class="form-control" name="terms">
					</div>

					<button type="submit" class="btn btn-success">Search</button>
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
		Categories : ${categories.size()} <br>
		<c:forEach var="category" items="${categories}">
    Category: ${category.key}  - Value: ${category.value} <br>
		</c:forEach>

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
						</div> <!-- caption --> <img
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

	<!--   <div class="bs-example">
        <input type="text" class="typeahead tt-query" autocomplete="on" spellcheck="false">
    </div>
-->




	<!-- Bootstrap core JavaScript
    ================================================== -->
	<!-- Placed at the end of the document so the pages load faster -->
	<script src="${pageContext.request.contextPath}/js/jquery.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/typeahead.js"></script>

</body>
</html>
