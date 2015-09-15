<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>

<html>
<body>
	<h2>Three Archives Search</h2>
	${message}
	<form
		action="${pageContext.request.contextPath}/archives/search_objects"
		method="post">

		<input type="text" name="terms"><br> <input type="submit"
			value="Search for Fedora objects" />
	</form>

	<!--  for loop going through the digital objects -->
	<c:forEach var="digitalObject" items="${objects}">
		<td>${digitalObject}</td>
		<!--  for loop going through the digital objects' datastreams -->
		<c:forEach var="datastream" items="${digitalObject.datastreams}">
			<!-- 	<td><img src="${ds.content}" style="width: 304px; height: 228px;"> -->
			<td>DatastreamID : ${datastream.datastreamID}</td>
			<br>
			<td>Datastream accessURL: ${datastream.content}</td>
			<c:choose>
				<c:when test="${datastream.datastreamID=='IMG'}">
					<td><img src="${datastream.content}"
						style="width: 304px; height: 228px;">
				</c:when>
				<c:when test="${datastream.datastreamID=='DC'}">
					<c:forEach var="dcDatastream" items="${dublinCoreDatastreams}">
						<c:if test="${dcDatastream.pid ==digitalObject.pid}">
    		${dcDatastream.dublinCoreMetadata}
    	</c:if>
					</c:forEach>
				</c:when>

				<c:otherwise>
   It is a different datastream<br>

				</c:otherwise>
			</c:choose>

		</c:forEach>
		<br>
	</c:forEach>

</body>
</html>
