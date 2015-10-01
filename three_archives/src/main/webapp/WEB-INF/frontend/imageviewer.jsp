<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@page import="common.fedora.Datastream"%>
<%@page import="common.fedora.DublinCoreDatastream"%>
<%@page import="common.fedora.FedoraDigitalObject"%>
<%@page import="search.SearchAndBrowseCategory"%>
<%@page import="common.fedora.DatastreamID"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashSet"%>
<%@page import="java.util.Set"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql"%>
<%@ taglib prefix="x" uri="http://java.sun.com/jsp/jstl/xml"%>
    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<!--<% String pic = request.getParameter("image"); %>-->
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/annotorious.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/annotorious.min.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie Snaps | Image View</title>
</head>
<body>
<h1>Name</h1>
<!--<img src="<%= pic %>">-->
<img src="${pageContext.request.contextPath}/images/kitten.jpg" class="annotatable"/>
<h4>Details</h4>

<script type="text/javascript">
function myFunction() {
	//session variable cart gets pid
}
</script>

<form name="map" method="post" action="${pageContext.request.contextPath}/archives/redirect_har_maps?image=images/kitten.jpg">
<!-- place word map in url-->
    <input type="submit" value="Place Me" />
</form>

</body>
</html>