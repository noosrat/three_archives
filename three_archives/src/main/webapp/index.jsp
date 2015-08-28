<%@page import="java.util.List"%>
<%@page
	import="com.yourmediashelf.fedora.generated.management.DatastreamProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>


<html>
<head>
<link rel="stylesheet" type="text/css" href="stylesheet.css"></link>

</head>
<body>

	<h2>Three Archives</h2>
	${message}
	<form
		action="${pageContext.request.contextPath}/archives/redirect_search"
		method="post">


		<input type="submit" value="Search" />

	</form>
	<form
		action="${pageContext.request.contextPath}/archives/redirect_exhibitions"
		method="post">


		<input type="submit" value="Exhibitions" />

	</form>
	<form
		action="${pageContext.request.contextPath}/archives/redirect_maps"
		method="post">
		<input type="submit" value="Maps" />
	</form>
</html>
