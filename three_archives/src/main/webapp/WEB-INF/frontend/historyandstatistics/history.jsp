
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
<link href="${pageContext.request.contextPath}/css/tagcloud.css"
	rel="stylesheet">
<link href="${pageContext.request.contextPath}/css/typeahead.css"
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

	<!-- Navigation -->
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
											class="typeahead tt-query tt-hint tt-dropdown-menu tt-suggestion"
											data-provider="typeahead" type="text"
											placeholder="Search Archive" value="${terms}"
											autocomplete="off" spellcheck="false" name="terms">
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
	</nav>


	<div class="container-fluid">
		<div class="row">
			<div class="col-sm-3">
				<!-- Left column -->
				<a href="#"><strong><i
						class="glyphicon glyphicon-wrench"></i> Tools</strong></a>

				<hr>
				<strong>Popular Searches</strong>
				<hr>
				<div class="panel panel-default">
					<div class="panel-body">
						<div id="demo">
							<div id="tagcloud">
								<c:forEach var="tag" items="${tagCloud}">
									<a
										href="${pageContext.request.contextPath}/archives/search_objects/category=SEARCH_ALL?terms=${tag.key}"
										rel="${tag.value}">${tag.key}</a>
								</c:forEach>
							</div>
						</div>
					</div>
				</div>

			</div>
			<!-- /col-3 -->
			<div class="col-sm-9">

				<!-- column 2 -->
				<hr>
				<!--/col-->
				<div class="col-md-8">
					<div class="panel panel-default">
						<div class="panel-heading">
							<h4>All updated items</h4>
						</div>
						<div class="panel-body">
							<section id="portfolio">
								<c:set var="count" value="0" scope="page" />
								<c:forEach var="digitalObject"
									items="${objectsModifiedSinceLastVisit}">
									<div
										class="col-lg-3 col-md-4 col-xs-6 portfolio-item thumbnail">
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
												<h3>${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}</h3>
												<br>

												<table>
													<td><img
														src="${digitalObject.datastreams['IMG'].content}"
														class="img-thumbnail img-responsive"
														alt="image unavailable"></td>

													<!-- can we not try just iterate through the dublinCoreDatastream's metadata -->
													<td><c:forEach var="dcMetadata"
															items="${digitalObject.datastreams['DC'].dublinCoreMetadata}">
															<c:if
																test="${dcMetadata.key!='IDENTIFIER' && dcMetadata.key!='TYPE' && dcMetadata.key!='FORMAT'}">
									${dcMetadata.key}: <a
																	href="${pageContext.request.contextPath}/archives/search_objects/category=${dcMetadata.key}?terms=${dcMetadata.value}">${dcMetadata.value}</a>
																<br>
															</c:if>

														</c:forEach></td>
												</table>
											</div>

											<button type="button">Place me</button>
										</div>
									</div>
								</div>
								<c:set var="count" value="${count + 1}" scope="page" />
							</c:forEach>
							</section>
						</div>
					</div>
					<!--/panel content-->
				</div>
				<!-- HERE -->

				<div class="row">
					<!-- center left-->
					<div class="col-md-4">
						<div class="well">
							Recent Updates <span class="badge pull-right">3</span>
						</div>

						<c:forEach var="updatedCategory"
							items="${categoriesWithRecentUpdates}">
							<div class="panel panel-default">
								<div class="panel-heading">
									<h3 class="panel-title">${updatedCategory.key}</h3>
								</div>
								<div class="panel-body">
									<!-- <div class="list-group list-inline pull-left"> -->
										<c:forEach var="updatedValue"
											items="${updatedCategory.value}">
											<a href="${pageContext.request.contextPath}/archives/browse?category=${updatedCategory.key}&${updatedCategory.key}=${updatedValue}" class="list-group-item"><span class="badge">23
													minutes ago</span> ${updatedValue}</a>
										</c:forEach>
									<!-- </div>-->
								</div>
							</div>
						</c:forEach>
					</div>

					<!--/panel-->


					<!--/panel-->

				</div>
				<!--/col-span-6-->

			</div>
			<!--/row-->
		</div>
		<!--/col-span-9-->
	</div>
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