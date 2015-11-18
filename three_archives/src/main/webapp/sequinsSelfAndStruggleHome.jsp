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
<link rel="stylesheet"
	href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
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
<style>
.carousel-inner>.item>img, .carousel-inner>.item>a>img {
	margin: auto;
}

.Sequins {
	width: 150px;
	height: 100px;
	background-color: #ffffff;
	border: 1px solid black;
	opacity: 0.8;
}

h3 {
	margin: 5%;
	font-weight: bold;
	color: #000000;
}
</style>
</head>

<body>

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

	<div class="container">
		<p></p>
		<br> <br> <br>
		<div class="row">
			<div class="col-lg-12" style="text-align: center">
				<h2>About Sequins, Self & Struggle</h2>
				<div class="well well-lg" style="background-color: #FFF0F0">
					<p>This project is a collaboration among the Departments of
						Drama at Royal Holloway and Queen Mary, University of London (UK),
						The Centre for Curating the Archive and the Centre for African
						Studies at the University of Cape Town, Africana Studies at Brown
						University (US) and the District 6 Museum. The primary aims are to
						research, document and disseminate archives of the Spring Queen
						and Miss Gay Western Cape pageants performed by disparate
						'coloured' communities in greater Cape Town.</p>

				</div>
			</div>
		</div>
		<div class="row">
			<div class="col-lg-12" style="text-align: center">
				<div id="myCarousel" class="carousel slide" data-ride="carousel">
					<!-- Indicators -->
					<ol class="carousel-indicators">
						<li data-target="#myCarousel" data-slide-to="0" class="active"></li>
						<li data-target="#myCarousel" data-slide-to="1"></li>

					</ol>

					<!-- Wrapper for slides -->
					<div class="carousel-inner" role="listbox">

						<div class="item active">
							<div class="row">
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/10.jpg"
										width="200">
								</div>
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/15.jpg"
										width="200">
								</div>
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/2.jpg"
										width="200">
								</div>
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/3.jpg"
										width="200">
								</div>
							</div>

						</div>

						<div class="item">
							<div class="row">
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/5.jpg"
										width="200">
								</div>
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/6.jpg"
										width="200">
								</div>
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/7.jpg"
										width="200">
								</div>
								<div class="col-lg-3" style="text-align: center">
									<img src="${pageContext.request.contextPath}/images/8.jpg"
										width="200">
								</div>
							</div>

						</div>



					</div>

					<!-- Left and right controls -->
					<a class="left carousel-control" href="#myCarousel" role="button"
						data-slide="prev"> <span
						class="glyphicon glyphicon-chevron-left" aria-hidden="true"></span>
						<span class="sr-only">Previous</span>
					</a> <a class="right carousel-control" href="#myCarousel" role="button"
						data-slide="next"> <span
						class="glyphicon glyphicon-chevron-right" aria-hidden="true"></span>
						<span class="sr-only">Next</span>
					</a>
				</div>
			</div>
		</div>
		<br> <br> 
		<div class="row">
			<div class="col-lg-12" style="text-align: center">
				<h2>Contribute to the archive</h2>
				<div class="well well-lg" style="background-color: #FFF0F0">
					<h4>Please contact:</h4>
					<p>Centre for Curating the Archive, University of Cape Town:</p>
					<p>www.cca.uct.ac.za</p>
					<p>hum-cca.uct.ac.za</p>
					<p>021 480 7151</p>

				</div>
			</div>
		</div>
		<!-- /.row -->
	</div>
	<div class="modal fade" id="login" role="dialog" style="z-index: 30000">
		<form role="form" method="post"
			action="${pageContext.request.contextPath}/archives/auth_user">
			<div class="modal-dialog">
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title">Enter details</h4>
					</div>
					<div class="modal-body">

						<div class="form-group">
							<label for="username">User name:</label> <input
								class="form-control" id="username" name="new_username">
						</div>
						<div class="form-group">
							<label for="pwd">Password:</label> <input type="password"
								class="form-control" id="pwd" name="new_pwd">
						</div>

					</div>
					<div class="modal-footer">
						<input type="submit" value="Submit" class="btn btn-primary btn-sm"
							name="authorise" />
					</div>

				</div>

			</div>
		</form>

	</div>

	<nav class="navbar navbar-inverse navbar-fixed-bottom navbar-fluid"
		role="navigation">
		<div class="container-fluid">
			<!-- Brand and toggle get grouped for better mobile display -->
			<div class="navbar-header">
				<div class="row">
					<div class="navbar-header col-sm-6">
						<a class="navbar-brand" href="${pageContext.request.contextPath}"><span
							class="glyphicon glyphicon-home"></span> Personal Histories</a>
					</div>
					<%
						if (session.getAttribute("USER") == null) {
					%>
					<div class="col-sm-5"></div>
					<div class="col-sm-1">
						<a data-toggle="modal" data-target="#login" class="navbar-brand" href="#login">Login</a>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("incorrect")) {
					%>
					<div class="col-sm-5">
						<div class="navbar-brand">Credentials incorrect</div>
					</div>
					<div class="col-sm-1">
						<a data-toggle="modal" data-target="#login"
							class="navbar-brand" href="#login">Login</a>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("false")) {
					%>
					<div class="col-sm-5"></div>
					<div class="col-sm-1">
						<a data-toggle="modal" data-target="#login"
							class="navbar-brand" href="#login">Login</a>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("ADMINISTRATOR")) {
					%>
					<div class="col-sm-5">
						<div class="navbar-brand">Logged on as Administrator</div>
					</div>
					<div class="col-sm-1">
						<form role="form" method="post"
							action="${pageContext.request.contextPath}/archives/logout_user">
							<input type="submit" value="logout"
								class="btn btn-primary btn-xs" name="logout" />
						</form>
					</div>
					<%
						} else if (session.getAttribute("USER").equals("privileged")) {
					%>
					<div class="col-sm-5">
						<div class="navbar-brand">Logged on as a privileged user</div>
					</div>
					<div class="col-sm-1">
						<form role="form" method="post"
							action="${pageContext.request.contextPath}/archives/logout_user">
							<input type="submit" value="logout" class="btn btn-primary btn-xs" name="logout" />
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
