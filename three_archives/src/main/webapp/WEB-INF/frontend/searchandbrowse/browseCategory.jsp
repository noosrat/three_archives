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
<link href="${pageContext.request.contextPath}/css/typeahead.css"
	rel="stylesheet">
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
					<a class="navbar-brand"
						href="${pageContext.request.contextPath}/archives/${ARCHIVE_CONCAT}">${ARCHIVE}</a>
				</div>
				<div id="navbar" class="collapse navbar-collapse"
					id="bs-example-navbar-collapse-1">
					>
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
									<c:set var="first" value="true" />
									<c:forEach var="searchCategory" items="${searchCategories}">
										<c:if test="${first ne 'true'}">
											<li><a
												href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>
										</c:if>
										<c:set var="first" value="false" />
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
												placeholder="Search Archive" 
												autocomplete="off" spellcheck="false" name="terms">
										</div>
									</div>
									<button type="submit" class="btn btn-default">
										<i class="glyphicon glyphicon-search"></i>
									</button>
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
					<hr>
					<c:forEach var="category" items="${categoriesAndObjects}">
						<li><a
							href="${pageContext.request.contextPath}/archives/browse?category=${category.key}"
							data-toggle="collapse" data-target="#demo${count}">
								${category.key} <span class="badge" style="float: right;">${fn:length(category.value)}</span>
						</a></li>
					</c:forEach>
					<p></p>
					<p></p>
					<c:if test="${not empty SERVICES['History']}">

						<li><a href="javascript:;" data-toggle="collapse"
							data-target="#demo"><i class="fa fa-fw fa-arrows-v"></i>
								RECENT UPDATES<i class="fa fa-fw fa-caret-down"><span
									class="badge" style="float: right;">${fn:length(objectsModifiedSinceLastVisit)}</span></i></a>
							<ul id="demo" class="collapse">
								<c:forEach var="update"
									items="${userFavouriteCategoriesWithRecentUpdates}">
									<ul>
										<font color="grey">${update}</font>
									</ul>
								</c:forEach>
								<c:if test="${fn:length(objectsModifiedSinceLastVisit) >0}">
									<li><a
										href="${pageContext.request.contextPath}/archives/history">Explore
											Updates</a></li>

								</c:if>
							</ul></li>



					</c:if>
				</ul>
			</div>

		</nav>
	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid" role="navigation">
        <div class="container-fluid">
            <!-- Brand and toggle get grouped for better mobile display -->
            <div class="navbar-header">
<a class="navbar-brand" href="${pageContext.request.contextPath}"><span
							class="glyphicon glyphicon-home"></span> Personal Histories</a>
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
					</div>
				</div>
				<h4 class="page-header">Browsing by Category: ${browseCategory}</h4>
				<c:if test="${browseCategory=='TITLE'}">
					<div class="col-lg-12">

						<c:forEach var="searchTag" items="${alphaTags}">
							<a class="label label-default"
								href="${pageContext.request.contextPath}/archives/browse?category=TITLE&TITLE=${searchTag}">${searchTag}</a>
						</c:forEach>
					</div>
				</c:if>

				<section id="portfolio">
					<c:forEach var="subCategory"
						items="${categoriesAndObjects[browseCategory]}">

						<div class="col-lg-3 col-md-4 col-xs-6 portfolio-item thumbnail" style="overflow:hidden; width:300px; height:250px; background-color:#D4CB90">
						
						<p align="center">${subCategory.key}<br> Items: <span class="badge">${fn:length(subCategory.value)}</span><p>
							<a
								href="${pageContext.request.contextPath}/archives/browse?category=${browseCategory}&${browseCategory}=${subCategory.key}"
								class="portfolio-link" data-target="#">
										

								 <c:set var="conditionVariable" value="true" />
								<c:forEach var="object" items="${subCategory.value}">
									<c:if test="${conditionVariable eq 'true'}">
										<img class="img-responsive img-thumbnail"
											src="${object.datastreams['IMG'].content}"
											alt="album cover unavailable"
											style="width: 90%;">
									</c:if>
									<c:set var="conditionVariable" value="false" />
								</c:forEach>
							</a>
						</div>
					</c:forEach>
				</section>
				<!-- container fluid -->
			</div>
		</div>
		<!-- wrapper -->
		<script type="text/javascript"
			src="${pageContext.request.contextPath}/js/lightbox.js"></script>
</body>
</html>