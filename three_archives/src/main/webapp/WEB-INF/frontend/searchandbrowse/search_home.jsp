<%@page	import="common.fedora.Datastream"%>
<%@page	import="common.fedora.DublinCoreDatastream"%>
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
	   
		<input type="text" name="terms"><br>
		<input type="submit" value="Search for Fedora objects" />
	</form>

	<c:forEach var="digitalObject" items="${objects}">
	<!-- 	<td><img src="${ds.content}" style="width: 304px; height: 228px;"> -->
		<td>${digitalObject}</td>
	</c:forEach>

</body>
</html>
