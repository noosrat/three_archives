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
			var words = "/data/SequinsSelfandStruggle.json";
		} else if (archive == "Movie Snaps") {
			var words = "/data/MovieSnaps.json";
		} else if (archive == "Harfield Village") {
			var words = "/data/HarfieldVillage.json";
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
								href="${pageContext.request.contextPath}/archives/SequinsSelfandStruggle">${ARCHIVE}</a>
						</c:when>
						<c:when test="${ARCHIVE =='Harfield Village'}">

							<a class="navbar-brand"
								href="${pageContext.request.contextPath}/archives/HarfieldVillage">${ARCHIVE}</a>
						</c:when>
						<c:when test="${ARCHIVE =='Movie Snaps'}">

							<a class="navbar-brand"
								href="${pageContext.request.contextPath}/archives/MovieSnaps">${ARCHIVE}</a>
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
									<li><a href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>
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


					<li><a
						href="${pageContext.request.contextPath}/archives/browse"
						data-toggle="collapse"
						data-target="${pageContext.request.contextPath}/archives/browse"><i
							class="fa fa-fw fa-arrows-v"></i>BROWSE ALL <i
							class="fa fa-fw fa-caret-down"></i> </a></li>

					<c:forEach var="category" items="${categoriesAndObjects}">
						<li><a href="${pageContext.request.contextPath}/archives/browse?category=${category.key}" data-toggle="collapse" data-target="#demo${count}"> <i class="fa fa-fw fa-arrows-v"></i>${category.key}
								<span class="badge">${fn:length(category.value)}</span><i class="fa fa-fw fa-caret-down"></i></a> 
						</li>
					</c:forEach>
										<li>

						<form action="${pageContext.request.contextPath}/archives/history"
							method="post">

							<input type="submit" value="While you were away..." />

						</form>
					</li>
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


				<c:forEach var="searchTag" items="${searchTags}">
						<a class="btn btn-primary btn-xs" href="${pageContext.request.contextPath}/archives/search_objects/category=SEARCH_ALL?terms=${searchTag}">${searchTag}</a>
				</c:forEach>
<br><br>

				<section id="portfolio">
					<div class="container">
						<c:set var="count" value="0" scope="page" />
						<c:forEach var="digitalObject" items="${objectsForArchive}">
							<div class="col-xs-8 col-sm-6 portfolio-item">
								<a href="#lightbox${count}" class="portfolio-link"
									data-toggle="modal" data-target="#lightbox${count}">

									<div class="caption">
										<div class="caption-content">
											[VIEW MORE DETAILS]
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
				<c:forEach var="digitalObject" items="${objectsForArchive}">
					<div id="lightbox${count}" class="modal fade" tabindex="-1"
						role="dialog" aria-labelledby="myLargeModalLabel"
						aria-hidden="true">
						<div class="modal-dialog">
							<div class="modal-content">
								<div class="modal-body">
								<h3>${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}</h3>
								<br>
								
									<!-- 	<table style="width: 100%"> <td>-->
									<img src="${digitalObject.datastreams['IMG'].content}"
										class="img-thumbnail img-responsive" alt="image unavailable">
									
									<!-- can we not try just iterate through the dublinCoreDatastream's metadata -->
									<c:forEach var="dcMetadata" items="${digitalObject.datastreams['DC'].dublinCoreMetadata}">
									<c:if test="${dcMetadata.key!='IDENTIFIER' && dcMetadata.key!='TYPE' && dcMetadata.key!='FORMAT'}">
									${dcMetadata.key}: <a href="${pageContext.request.contextPath}/archives/search_objects/category=${dcMetadata.key}?terms=${dcMetadata.value}">${dcMetadata.value}</a><br>
									</c:if>
									
									</c:forEach>
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