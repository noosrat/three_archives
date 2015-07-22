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

	<form action="${pageContext.request.contextPath}/archives/search"
		method="post">
		<input type="submit" value="Search for Fedora objects" /> <input
			type="text" name="terms">
	</form>



	<c:forEach var="datastreamProfile" items="${objects}">
		<td><img src="${datastreamProfile.dsLocation}"
			style="width: 304px; height: 228px;"></td>
	</c:forEach>




</body>
</html>
