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
<link href="${pageContext.request.contextPath}/css/typeahead.css"
	rel="stylesheet">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/search_and_browse.css"></link>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/jquery-1.11.3.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/bootstrap-3.3.5/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/js/typeahead.js"></script>

<script type="text/javascript">
$(document).ready(function() {
		var archive ='<%=session.getAttribute("ARCHIVE_CONCAT")%>';
		var words = "/data/" + archive + ".json";
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
			source : countries.ttAdapter(),

		});
	});
</script>

</head>


<body>
	<div id="wrapper">
		<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
			<div class="container-fluid">


				<div class="navbar-header">
					<a class="navbar-brand"
						href="${pageContext.request.contextPath}/archives/${ARCHIVE_CONCAT}">${ARCHIVE}</a>
				</div>
				<div id="navbar" class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">>
				<ul class="nav navbar-nav">
					<c:forEach var="service" items="${SERVICES}">
						<c:if test="${service.value!='' && service.value!= ' '}">
							<li><a
								href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
							<li>
						</c:if>
					</c:forEach>

				</ul>
					<c:if test="${not empty SERVICES['Browse']}">
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
											placeholder="Search Archive" value="${terms}" autocomplete="off"
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
				</c:if>
				</div>
			</div>
			<!-- side bar -->


			<div class="collapse navbar-collapse navbar-ex1-collapse">
				<ul class="nav navbar-nav side-nav">


					<li><a
						href="${pageContext.request.contextPath}/archives/browse"
						data-toggle="collapse"
						data-target="${pageContext.request.contextPath}/archives/browse"><i
							class="fa fa-fw fa-arrows-v"></i>BROWSE ALL <i
							class="fa fa-fw fa-caret-down"></i> </a></li>

					<c:forEach var="category" items="${categoriesAndObjects}">
						<li><a
							href="${pageContext.request.contextPath}/archives/browse?category=${category.key}"
							data-toggle="collapse" data-target="#demo${count}"> ${category.key} <span
								class="badge">${fn:length(category.value)}</span></a></li>
					</c:forEach>
					
					<c:if test="${not empty SERVICES['History']}">
					<li>

					<font color="white">
					RECENT UPDATES:
					${fn:length(objectsModifiedSinceLastVisit)} updated since your last visit
					<br>
					<c:forEach var="update" items="${userFavouriteCategoriesWithRecentUpdates}">
						<li>
						${update}
						</li>
					
					</c:forEach>
					have been updated.  <a href="${pageContext.request.contextPath}/archives/history" >Explore</a> more updates.
					</font>
					</li>
					</c:if>
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
						
						<div>
							<h4> <span
								class="badge">${fn:length(objectsForArchive)}</span> results returned for
								search : "${terms}"</h4>
							<table style="width:100%">
							<td align="right"><a class="btn btn-primary" href="${pageContext.request.contextPath}/archives/browse/ORDER_BY=TITLE">sort results by title</a> 
							<a class="btn btn-primary" href="${pageContext.request.contextPath}/archives/browse/ORDER_BY=YEAR">sort results by year</a></td>
							</table>
						</div>
						
						<ol class="breadcrumb">
							<li><i class="fa fa-dashboard"></i>${browseCategory}</li>
							<li class="active"><i class="fa fa-wrench"></i>
								${categoryValue}</li>
						</ol>
					</div>
				</div>


<div class="col-lg-12">
				<c:forEach var="searchTag" items="${searchTags}">
					<!-- <a class="btn btn-primary btn-xs" -->
					<a class="label label-default"	href="${pageContext.request.contextPath}/archives/search_objects/category=SEARCH_ALL?terms=${searchTag}">${searchTag}</a>
				</c:forEach>
				</div>
				<br> <br>

				<section id="portfolio">
					<div class="container-fluid"> 
					<c:set var="count" value="0" scope="page" />
					<c:forEach var="digitalObject" items="${objectsForArchive}">
						<div class="col-lg-3 col-sm-4 col-xs-6 portfolio-item">
							<a href="#lightbox${count}"
								data-toggle="modal" data-target="#lightbox${count}">

								<!-- <div class="caption">
									<div class="caption-content">[VIEW MORE DETAILS]</div>
								</div> --> <!-- caption --> <img
								src="${digitalObject.datastreams['IMG'].content}"
								class="img-thumbnail img-responsive" alt="image unavailable" style="height: 300px; width: 100%; display: block;">
<!--  								class="img-thumbnail img-responsive" alt="image unavailable" style="width: 300px; max-height: 500px"> -->
							</a>
						</div>
						<c:set var="count" value="${count + 1}" scope="page" />
					</c:forEach>
					<!-- 			</div> -->
				</section>
				<c:set var="count" value="0" scope="page" />
				<c:forEach var="digitalObject" items="${objectsForArchive}">
					<div id="lightbox${count}" class="modal fade" tabindex="-1"
						role="dialog" aria-labelledby="myLargeModalLabel"
						aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-body">
									<h3>${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}</h3>
									<br>

									<table>
										<td><img
											src="${digitalObject.datastreams['IMG'].content}"
											class="img-thumbnail img-responsive" alt="image unavailable"></td>

										<!-- can we not try just iterate through the dublinCoreDatastream's metadata -->
									<td><c:forEach var="dcMetadata" items="${digitalObject.datastreams['DC'].dublinCoreMetadata}">
									<c:if test="${dcMetadata.key!='IDENTIFIER' && dcMetadata.key!='TYPE' && dcMetadata.key!='FORMAT' && dcMetadata.key!='COVERAGE' && dcMetadata.key!='ANNOTATIONS'}">
									${dcMetadata.key}: <a href="${pageContext.request.contextPath}/archives/search_objects/category=${dcMetadata.key}?terms=${dcMetadata.value}">${dcMetadata.value}</a><br>
												</c:if>

											</c:forEach></td>
									</table>
								</div>

								<form name="map" method="post" action="${pageContext.request.contextPath}/archives/redirect_maps/place?image=${digitalObject.pid}">
									<!-- place word map in url-->
   									<input type="submit" value="Place Me" />
								</form>
							</div>
						</div>
					</div>
					<c:set var="count" value="${count + 1}" scope="page" />
				</c:forEach>
			</div></div>
			<!-- container fluid -->
		</div>
	</div>
	<!-- wrapper -->
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/js/lightbox.js"></script>
</body>
</html>