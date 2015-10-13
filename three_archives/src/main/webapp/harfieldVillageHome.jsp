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
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
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

	<!-- Navigation -->
	<!-- Navigation -->
	<nav class="navbar navbar-inverse navbar-fixed-top navbar-left"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<a class="navbar-brand"
					href="${pageContext.request.contextPath}/archives/${ARCHIVE_CONCAT}">${ARCHIVE}</a>
			</div>
			<div class="collapse navbar-collapse"
				id="bs-example-navbar-collapse-1">
				<ul class="nav navbar-nav">
					<c:forEach var="service" items="${SERVICES}">
						<c:if test="${service.value!='' && service.value!= ' '}">

							<c:choose>
								<c:when test="${service.key eq 'Uploads'}">
									<%
										if (session.getAttribute("USER") != null) {
															if (session.getAttribute("USER").equals("ADMINISTRATOR")) {
									%>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<%
										}
														}
									%>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<li>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
					<%
						if (session.getAttribute("USER") != null) {
							if (session.getAttribute("USER").equals("ADMINISTRATOR")) {
					%>


					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_user">Users</a></li>
					<%
						}
						}
					%>

				</ul>
				<c:if test="${not empty SERVICES['Browse']}">
					<ul class="nav navbar-nav navbar-right top-nav">
						<!-- <li class="dropdown"><a
							href="${pageContext.request.contextPath}/archives/search_objects"
							class="dropdown-toggle" data-toggle="dropdown" role="button"
							aria-haspopup="true" aria-expalinded="false">${searchCategories[0]}<span
								class="caret"></span></a>
							<ul class="dropdown-menu">
								<c:set var="first" value="true"/>
								<c:forEach var="searchCategory" items="${searchCategories}">
								<c:if test="${first ne 'true'}">
									<li><a
										href="${pageContext.request.contextPath}/archives/search_objects/category=${searchCategory}">${searchCategory}</a></li>
										</c:if>
								<c:set var="first" value="false"/>
								</c:forEach>
							</ul></li>
 -->


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
								<button type="submit" class="btn btn-default">
									<i class="glyphicon glyphicon-search"></i>
								</button>

							</form>
						</li>
					</ul>
				</c:if>
			</div>
		</div>
	</nav>

	<br>
	<br>
	<br>
	<br>
	<div class="container">

		<div class="row">
			<div class="col-md-3" id="leftCol">

				<div class="well"
					style="background-color: #D4CB90; opacity: 0.8; color: black">
					<ul class="nav nav-stacked" id="sidebar">
						<li><a href="#sec0">About Harfield Village</a></li>
						<li><a href="#sec1">Map</a></li>
						<li><a href="#sec3">Contact</a></li>
					</ul>
				</div>

			</div>
			<div class="col-md-9">
				<h2 id="sec0">About Harfield Village</h2>
				<p>The Harfield Village archive is a collection of artefacts
					comprising of images, videos, letters, statements and accounts of
					residents who were forcibly-removed from the Claremont suburb
					during the times of apartheid in South Africa .</p>
				<hr>
				<h2 id="sec1">Map</h2>
				<p>
				<div class="row">
					<div class="col-lg-12">
						<img src="${pageContext.request.contextPath}/images/harfieldV.png" class="img-responsive">
					</div>
				</div></p>
				<hr>


				<hr>

				<h2 id="sec3">Contact</h2>
				<table style="width: 100%">

					<td><br>University of Cape Town<br> 31 -37 Orange
						Street<br> Gardens, 8001<br> Cape Town, South Africa<br>
						Ph 021 480 7151<br> email: jm.higgins@uct.ac.za</td>
					<td><img
						src="${pageContext.request.contextPath}/images/CCALogo.png"
						style="width: 70%"></td>

   <br><br><br><br><br>
				</table>

			</div>
		</div>
	</div>
	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">


				<div class="row">
					<div class="navbar-header col-sm-8">
						<a class="navbar-brand" href="${pageContext.request.contextPath}"><span
							class="glyphicon glyphicon-home"></span> Personal Histories</a>
					</div>
					<%
						if (session.getAttribute("USER") == null) {
					%>
					<div class="col-sm-3"></div>
					<div class="col-sm-1">
						<a style="margin: 15px" data-toggle="modal" data-target="#login"
							class="navbar-brand" href="#login">Login</a>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("incorrect")) {
					%>
					<div class="col-sm-3">
						<div class="navbar-brand">Credentials incorrect</div>
					</div>
					<div class="col-sm-1">
						<a style="margin: 15px" data-toggle="modal" data-target="#login"
							class="navbar-brand" href="#login">Login</a>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("false")) {
					%>
					<div class="col-sm-3"></div>
					<div class="col-sm-1">
						<a style="margin: 15px" data-toggle="modal" data-target="#login"
							class="navbar-brand" href="#login">Login</a>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("ADMINISTRATOR")) {
					%>
					<div class="col-sm-3">
						<div class="navbar-brand">Logged on as Administrator</div>
					</div>
					<div class="col-sm-1">
						<form role="form" method="post"
							action="${pageContext.request.contextPath}/archives/logout_user">
							<input style="margin: 15px" type="submit" value="logout"
								class="btn btn-primary btn-xs" name="logout" />
						</form>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("privileged")) {
					%>
					<div class="col-sm-3">
						<div style="margin-left: 600px;" class="navbar-brand">Logged
							on as a privileged user</div>
					</div>
					<div class="col-sm-1">
						<form role="form" method="post"
							action="${pageContext.request.contextPath}/archives/logout_user">
							<input style="margin: 15px" type="submit" value="logout"
								class="btn btn-primary btn-xs" name="logout" />
						</form>
					</div>

					<%
						}
					%>
				</div>

			</div>
		</div>
		<!-- /.container -->
	</nav>
	<style>
.ad {
	position: absolute;
	bottom: 70px;
	right: 48px;
	z-index: 992;
	background-color: #f3f3f3;
	position: fixed;
	width: 155px;
	padding: 1px;
}

.ad-btn-hide {
	position: absolute;
	top: -10px;
	left: -12px;
	background: #fefefe;
	background: rgba(240, 240, 240, 0.9);
	border: 0;
	border-radius: 26px;
	cursor: pointer;
	padding: 2px;
	height: 25px;
	width: 25px;
	font-size: 14px;
	vertical-align: top;
	outline: 0;
}

.carbon-img {
	float: left;
	padding: 10px;
}

.carbon-text {
	color: #888;
	display: inline-block;
	font-family: Verdana;
	font-size: 11px;
	font-weight: 400;
	height: 60px;
	margin-left: 9px;
	width: 142px;
	padding-top: 10px;
}

.carbon-text:hover {
	color: #666;
}

.carbon-poweredby {
	color: #6A6A6A;
	float: left;
	font-family: Verdana;
	font-size: 11px;
	font-weight: 400;
	margin-left: 10px;
	margin-top: 13px;
	text-align: center;
}
</style>
	<div class="ad collapse in">
		<button class="ad-btn-hide" data-toggle="collapse" data-target=".ad">&times;</button>
		<script async type="text/javascript"
			src="//cdn.carbonads.com/carbon.js?zoneid=1673&serve=C6AILKT&placement=bootplycom"
			id="_carbonads_js"></script>
	</div>

</body>

</body>

</html>
