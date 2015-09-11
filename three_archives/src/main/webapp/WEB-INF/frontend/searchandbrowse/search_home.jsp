<%@page	import="common.fedora.Datastream"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<html>
<body>
	<h2>Three Archives Search</h2>
	${message}
	<form action="${pageContext.request.contextPath}/archives/search_objects"
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

	<c:forEach var="ds" items="${objects}">
		<td><img src="${ds.content}" style="width: 304px; height: 228px;">
			</td>
	</c:forEach>

</body>
</html>
