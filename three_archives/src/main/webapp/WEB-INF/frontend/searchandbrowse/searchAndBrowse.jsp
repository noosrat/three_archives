<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="search.SearchAndBrowseCategory"%>
<%@page import="common.fedora.DatastreamID"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">

<title>Three Archives : Search and Browse</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/search_and_browse.css"></link>

<link
	href="${pageContext.request.contextPath}/bootstrap-3.3.5/css/bootstrap.min.css"
	rel="stylesheet">
<!-- <link href="${pageContext.request.contextPath}/css/lightbox.css"
	rel="stylesheet">-->
<link href="${pageContext.request.contextPath}/theme/css/sb-admin.css"
	rel="stylesheet">
<link
	href="${pageContext.request.contextPath}/css/bootstrap-lightbox.min.css"
	rel="stylesheet">

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/bootstrap-3.3.5/js/bootstrap.js"></script>
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
	<div id="wrapper">
		<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container-fluid">


				<div class="navbar-header">

					<c:choose>
						<c:when test="${ARCHIVE =='Sequins, Self and Struggle'}">

							<a class="navbar-brand"
								href="${pageContext.request.contextPath}/archives/redirect_sequins">${ARCHIVE}</a>
						</c:when>
						<c:when test="${ARCHIVE =='Harfield Village'}">

							<a class="navbar-brand"
								href="${pageContext.request.contextPath}/archives/redirect_harfield">${ARCHIVE}</a>
						</c:when>
						<c:when test="${ARCHIVE =='Movie Snaps'}">

							<a class="navbar-brand"
								href="${pageContext.request.contextPath}/archives/redirect_snaps">${ARCHIVE}</a>
						</c:when>
					</c:choose>
				</div>







				<div id="navbar" class="collapse navbar-collapse">
					<ul class="nav navbar-nav navbar-right top-nav">
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
			<!-- side bar -->
			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav side-nav">
					
					
					<li><a href="${pageContext.request.contextPath}/archives/browse" data-toggle="collapse"
							data-target="${pageContext.request.contextPath}/archives/browse"><i class="fa fa-fw fa-arrows-v"></i>BROWSE ALL
								<i
								class="fa fa-fw fa-caret-down"></i> </a>
					</li>
					
					<c:set var="count" value="0" scope="page" />
					<c:forEach var="category" items="${browseCategories}">
						<li><a href="javascript:;" data-toggle="collapse"
							data-target="#demo${count}"><i class="fa fa-fw fa-arrows-v"></i>${category.key}
								<span class="badge">${fn:length(category.value)}</span><i
								class="fa fa-fw fa-caret-down"></i> </a>
							<ul id="demo${count}" class="collapse">
								<c:forEach var="categoryValue" items="${category.value}">
									<li><a
										href="${pageContext.request.contextPath}/archives/browse?category=${category.key}&${category.key}=${categoryValue}">${categoryValue}</a></li>
								</c:forEach>

							</ul></li>
						<c:set var="count" value="${count + 1}" scope="page" />
					</c:forEach>
				</ul>
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

		<div id="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h3 class="page-header">
							<small>${message}</small>
						</h3>
						<ol class="breadcrumb">
							<li><i class="fa fa-dashboard"></i>${browseCategory}</li>
							<li class="active"><i class="fa fa-wrench"></i>
								${categoryValue}</li>
						</ol>
					</div>
				</div>


				Other search tags: <br>
				<c:forEach var="searchTag" items="${searchTags}">
					<a
						href="${pageContext.request.contextPath}/archives/search_objects/category=SEARCH_ALL?terms=${searchTag}">${searchTag}</a>
				</c:forEach>

				<section id="portfolio">
					<div class="container">
						<c:set var="count" value="0" scope="page" />
						<c:forEach var="digitalObject" items="${objects}">
							<div class="col-xs-8 col-sm-6 portfolio-item">
								<a href="#lightbox${count}" class="portfolio-link"
									data-toggle="modal" data-target="#lightbox${count}">

									<div class="caption">
										<div class="caption-content">
											<br> Title:
											${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}
											<br> Type:
											${digitalObject.datastreams['DC'].dublinCoreMetadata['FORMAT']}
										</div>
										<!-- caption content -->
									</div> <!-- caption --> <img
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
					<div id="lightbox${count}" class="modal fade" tabindex="-1"
						role="dialog" aria-labelledby="myLargeModalLabel"
						aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-body">
									<!-- 	<table style="width: 100%"> <td>-->
									<img src="${digitalObject.datastreams['IMG'].content}"
										class="img-thumbnail img-responsive" alt="image unavailable">
									</td> Title: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=TITLE?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}</a><br>
									Collection: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=COLLECTION?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['COLLECTION']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['COLLECTION']}</a><br>
									Contributor: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=CONTRIBUTOR?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['CONTRIBUTOR']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['CONTRIBUTOR']}</a><br>
									Coverage:
									${digitalObject.datastreams['DC'].dublinCoreMetadata['COVERAGE']}<br>
									Creator:
									${digitalObject.datastreams['DC'].dublinCoreMetadata['CREATOR']}
									<br> Date: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=YEAR?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['DATE']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['DATE']}</a>
									<br> Description: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=DESCRIPTION?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['DESCRIPTION']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['DESCRIPTION']}</a><br>
									Event: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=EVENT?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['EVENT']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['EVENT']}</a><br>
									Format:
									${digitalObject.datastreams['DC'].dublinCoreMetadata['FORMAT']}
									<br> Location: <a
										href="${pageContext.request.contextPath}/archives/search_objects/category=LOCATION?terms=${digitalObject.datastreams['DC'].dublinCoreMetadata['LOCATION']}">${digitalObject.datastreams['DC'].dublinCoreMetadata['LOCATION']}</a><br>
									Subject:
									${digitalObject.datastreams['DC'].dublinCoreMetadata['SUBJECT']}
									<br>


								</div>

								<button type="button">Place me</button>
							</div>
						</div>
					</div>
					<c:set var="count" value="${count + 1}" scope="page" />
				</c:forEach>
			</div>
			<!-- container fluid -->
		</div>
	</div>
	<!-- wrapper -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/lightbox.js"></script>
</body>
</html>