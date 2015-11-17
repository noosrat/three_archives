<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="common.fedora.DatastreamID"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<title>Downloads</title>

<!-- Bootstrap Core CSS -->
<link
	href="${pageContext.request.contextPath}/bootstrap-3.3.5/css/bootstrap.min.css"
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
<meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 0px;
        padding: 0px
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAkDhZAIvYefYnMLplLMYFBxACznT-8lmA"></script>
    <!--<script src="${pageContext.request.contextPath}/js/markerclusterer.js" type="text/javascript"></script>-->
</head>
<body>
<script type="text/javascript">
//document.getElementById("nodeGoto").addEventListener("click", function() {
  //  gotoNode(result.name);
//}, false);
console.log(<%=request.getParameter("deletions")%>);
var remove=[];
function change(animal)
{
if (animal.checked==false){
	var thing = animal.getAttribute("data-pid");
	//alert(thing);
	//console.log(('#ms:1').value);
	remove.push(thing);}
else
	{
	console.log($(this).value);
	remove.splice(remove.indexOf($(this).attr("value")), 1);}

document.getElementById("demo").innerHTML = remove;
console.log(remove);
}
</script>

<div >
		<div class="container-fluid">
				<div class="row">
					<div class="col-lg-12">
						<h3 class="page-header">
						</h3>

						<div>
							<table style="width: 100%">
								<tr>
									<td></td>
								</tr>
								<tr>
									<td></td>
									<td align="right">
										<div>
											<form method="post" action="${pageContext.request.contextPath}/archives/downloads/checkout">
											<textarea id="demo" name="deletions" readonly=readonly style="display:none;"></textarea>
											<input type="submit" value="Download My Collection" name="checkout" class="btn btn-default"/>
												</form>
										</div>
									</td>

								</tr>
							</table>

						</div>
						<hr>
					</div>
				</div>
				</div>
				</div>
				

<div>
<section id="portfolio">
					<div class="container-fluid">
						<c:forEach var="digitalObject" items="${cart}">
							<div class="col-lg-3 col-sm-4 col-xs-6 portfolio-item">

								<div class="thumbnail">
									<div align="center"
										style="overflow: hidden; width: 300px; height: 250px">
										<img
											src="${digitalObject.datastreams['IMG'].content}"
											class="img-responsive img-thumbnail" alt="image unavailable"
											style="width: 95%; height: 100%">

									</div>



									<table style="width: 100%">
										<td align="center">${digitalObject.datastreams['DC'].dublinCoreMetadata['TITLE']}</td>
										<td align="right"><input align="right"
											id="${digitalObject.pid}" type="checkbox" data-pid ="${digitalObject.pid}"
											onchange="change(this)" checked/></td>
									</table>
								</div>

							</div>
							
						</c:forEach>
						</div>
						</section>
						</div>
						
	
<!-- http://localhost:8080/fedora/objects/ms:1/datastreams/IMG/content?download=true&format=xml -->


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
									<%--if (session.getAttribute("USER").equals("ADMINISTRATOR")){--%>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<%--}--%>
								</c:when>
								<c:otherwise>
									<li><a
										href="${pageContext.request.contextPath}/archives/${service.value}">${service.key}</a></li>
									<li>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:forEach>
					<!--add user stuff here-->
					<%--if (session.getAttribute("USER").equals("ADMINISTRATOR")){--%>

					<li><a
						href="${pageContext.request.contextPath}/archives/redirect_user">Users</a></li>
					<%--}--%>

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
	</nav>


	
				 <div id="map-canvas"></div>
	
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
