
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
<link href="${pageContext.request.contextPath}/css/tagcloud.css"
	rel="stylesheet">
	<link
	href="${pageContext.request.contextPath}/css/typeahead.css"
	rel="stylesheet">

<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/bootstrap-3.3.5/js/bootstrap.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.tagcloud.js"
	type="text/javascript" charset="utf-8"></script>
<script type="text/javascript" charset="utf-8">
	$.fn.tagcloud.defaults = {
		size : {
			start : 12,
			end : 20,
			unit : 'pt'
		},
		color : {
			start : '#cde',
			end : '#f52'
		}
	};

	$(function() {
		$('#tagcloud a').tagcloud();
	});
</script>
</head>


<body>
	<div id="wrapper">
		<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container-fluid">
				<div class="navbar-header">

					<a class="navbar-brand"
						href="${pageContext.request.contextPath}/archives/browse">${ARCHIVE}</a>

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

			<br> <br>
			<!-- side bar -->

		</nav>

		${message}



		<div id="page-wrapper">
			<div class="container-fluid">


				<section id="portfolio">
					<div class="container">
						<c:set var="count" value="0" scope="page" />
						<c:forEach var="digitalObject"
							items="${objectsModifiedSinceLastVisit}">
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
				<c:forEach var="digitalObject"
					items="${objectsModifiedSinceLastVisit}">
					<div id="lightbox${count}" class="modal fade" tabindex="-1"
						role="dialog" aria-labelledby="myLargeModalLabel"
						aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-body">
									<table style="width: 100%">
										<td><img
											src="${digitalObject.datastreams['IMG'].content}"
											class="img-thumbnail img-responsive" alt="image unavailable"></td>


										<td>Title: <a
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
										</td>

									</table>
								</div>

								<button type="button">Place me</button>
							</div>
						</div>
					</div>
					<c:set var="count" value="${count + 1}" scope="page" />
				</c:forEach>

				<br> <br>


				<div class="page-header">
					<h3>General Updates</h3>
				</div>
				<div class="row">
					<c:forEach var="updatedCategory"
						items="${categoriesWithRecentUpdates}">
						<div class="col-sm-4">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">${updatedCategory.key}</h3>
								</div>
								<c:forEach var="updatedValues" items="${updatedCategory.value}">
									<div class="panel-body">${updatedValues}</div>
								</c:forEach>
							</div>

						</div>
					</c:forEach>
				</div>

				<div class="page-header">
					<h3>Your favourite(top 3) category updates(if any)</h3>
				</div>
				<div class="row">

					<c:forEach var="favouriteCategoriesUpdates"
						items="${userFavouriteCategoriesWithRecentUpdates}">

						<div class="col-sm-4">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">${favouriteCategoriesUpdates}</h3>
								</div>
								<div class="panel-body">Value</div>
							</div>

						</div>
					</c:forEach>

				</div>
				<!-- container fluid -->

				<div id="container">
					<div id="demo">
						<div id="tagcloud">
							<c:forEach var="tag" items="${tagCloud}">
								<a href="${pageContext.request.contextPath}/archives/search_objects/category=SEARCH_ALL?terms=${tag.key}" rel="${tag.value}">${tag.key}</a>
							</c:forEach>
						</div>
					</div>
				</div>

				<br>
				<br>
				<br>
				<br>
			</div>
		</div>

		<!-- wrapper -->
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/lightbox.js"></script>
</body>
</html>
