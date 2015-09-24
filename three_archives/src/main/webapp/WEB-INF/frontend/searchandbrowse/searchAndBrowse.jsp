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
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css"
	rel="stylesheet">
<!-- <link href="${pageContext.request.contextPath}/css/lightbox.css"
	rel="stylesheet">-->
	<link href="${pageContext.request.contextPath}/css/bootstrap-lightbox.min.css" rel="stylesheet">



<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/typeahead.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	var archive = '<%=session.getAttribute("ARCHIVE")%>';
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
		<div class="container-fluid">
			<div class="navbar-header">

				<a class="navbar-brand"
					href="${pageContext.request.contextPath}/archives/browse">${ARCHIVE}</a>

			</div>


			<div id="navbar" class="collapse navbar-collapse">

				<form class="navbar-form navbar-right"
					action="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategories[0]}"
					method="post">
					<div class="form-group">
						<div id="prefetch">
							<input class="form-control typeahead tt-query tt-hint tt-dropdown-menu tt-suggestion"
								data-provider="typeahead" type="text"
								placeholder="Search Archive" autocomplete="off"
								spellcheck="false" name="terms">
						</div>
					</div>
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
					<button type="submit" class="btn">Search</button>

					<div class="checkbox">
						<label> <input type="checkbox" id="limitSearch"
							name="limitSearch" value="limitSearch"><font
							color="white"> Limit search to these results</font>
						</label>
					</div>

				</form>


			</div>
		</div>
	</nav>

	<nav class="navbar navbar-inverse navbar-fixed-bottom">
		<div class="container-fluid">
			<div class="navbar-header">
				<button type="button" class="navbar-toggle collapsed"
					data-toggle="collapse" data-target="#navbar" aria-expanded="false"
					aria-controls="navbar">
					<span class="sr-only">Toggle navigation</span> <span
						class="icon-bar"></span> <span class="icon-bar"></span> <span
						class="icon-bar"></span>
				</button>
				<a class="navbar-brand" href="#">Three Archives</a>

			</div>
		</div>
	</nav>
	${message}

	<div class="bs-example">
		<ul>
			<li>${browseCategory}> ${categoryValue}</li>

		</ul>
	</div>

	<div class="bs-docs-sidebar">
		<ul class="nav nav-list bs-docs-sidenav">
			<!-- <li><a href="#dropdowns"><i class="icon-chevron-right"></i> Dropdowns</a></li> -->
			<c:forEach var="category" items="${browseCategories}">
				<li class="dropdown"><a
					href="${pageContext.request.contextPath}/archives/browse?category=${category.key}"
					class="dropdown-toggle" data-toggle="dropdown" role="button"
					aria-haspopup="true" aria-expanded="false"><i
						class="icon-chevron-right"></i>${category.key}<span class="caret"></span></a>
					<ul class="dropdown-menu">
						<c:forEach var="categoryValue" items="${category.value}">
							<li><a
								href="${pageContext.request.contextPath}/archives/browse?category=${category.key}&${category.key}=${categoryValue}">${categoryValue}</a></li>
						</c:forEach>
					</ul></li>
			</c:forEach>

		</ul>
	</div>
Other search tags: <br>
	<c:forEach var="searchTag" items="${searchTags}">
		<a
			href="${pageContext.request.contextPath}/archives/search_objects/category=SEARCH_ALL?terms=${searchTag}">${searchTag}</a>
	</c:forEach>

	<section id="portfolio">
	<div class="container gallery">
		<c:set var="count" value="0" scope="page" />
		<c:forEach var="digitalObject" items="${objects}">
			<div class="col-xs-6 col-sm-3 portfolio-item">
				<a href="#lightbox${count}" class="portfolio-link" data-toggle="modal"
					data-target="#lightbox${count}"> 
					
					<div class="caption">
								<div class="caption-content">


									<br> Title:
									${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}
									<br> Type:
									${digitalObject.datastreams['DC'].dublinCoreMetadata['FORMAT']}

								</div>
								<!-- caption content -->
							</div> <!-- caption --> 
					
					
					<img
					src="${digitalObject.datastreams['IMG'].content}"
					class="img-thumbnail img-responsive" alt="image unavailable">
				</a>
			</div>
			<c:set var="count" value="${count + 1}" scope="page" />
		</c:forEach>
	</div>
	</section>
	<c:set var="count" value="0" scope="page" />
	<c:forEach var="digitalObject" items="${objects}">
		<div id="lightbox${count}" class="modal fade" tabindex="-1" role="dialog"
			aria-labelledby="myLargeModalLabel" aria-hidden="true">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-body">
					<table style="width:100%">
						<td><img
					src="${digitalObject.datastreams['IMG'].content}"
					class="img-thumbnail img-responsive" alt="image unavailable"></td>
						
						
				 <td>   Title: <a href="${pageContext.request.contextPath}/archives/search_objects/category=TITLE?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}</a><br> 
					
					Description: <a href="${pageContext.request.contextPath}/archives/search_objects/category=DESCRIPTION?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['DESCRIPTION']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['DESCRIPTION']}</a><br>
					Date: <a href="${pageContext.request.contextPath}/archives/search_objects/category=YEAR?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['DATE']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['DATE']}</a> <br>
					Format: ${digitalObject.datastreams['DC'].dublinCoreMetadata['FORMAT']} <br>
					Coverage: ${digitalObject.datastreams['DC'].dublinCoreMetadata['COVERAGE']}<br>
					Contributor: ${digitalObject.datastreams['DC'].dublinCoreMetadata['CONTRIBUTOR']}<br>
					Subject: ${digitalObject.datastreams['DC'].dublinCoreMetadata['SUBJECT']} <br>
					Creator: ${digitalObject.datastreams['DC'].dublinCoreMetadata['CREATOR']} <br>	
					</td>
					</table>
					</div>

				<button type="button">Place me</button>
				<button type="button" data-dismiss="modal"	aria-hidden="true">×</button>
				</div>
			</div>
		</div>
		<c:set var="count" value="${count + 1}" scope="page" />
	</c:forEach>


	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/lightbox.js"></script>


</body>
</html>