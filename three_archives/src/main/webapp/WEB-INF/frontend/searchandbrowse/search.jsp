<%@page import="java.util.List"%>
<%@page
	import="com.yourmediashelf.fedora.generated.management.DatastreamProfile"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<html>
<body>
	<h2>Three Archives Search</h2>
	${message}
	<form action="${pageContext.request.contextPath}/archives/search"
		method="post">
	  	<select id="query" name="query">
			<option value="all">All</option>
			<option value="pid">ID</option>
			<option value="title">Title</option>
			<option value="description">Description</option>
		</select>  
		<input type="text" name="terms"><br>
		<input type="submit" value="Search for Fedora objects" />
	</form>

	<c:forEach var="datastreamProfile" items="${objects}">
		<td><img src="${datastreamProfile.dsLocation}" style="width: 304px; height: 228px;">
			</td>
	</c:forEach>

</body>
</html>
