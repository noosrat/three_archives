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

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Downloads</title>
</head>
<body>

<%Set<FedoraDigitalObject> cart = (HashSet<FedoraDigitalObject>) request.getAttribute("cart");

for(FedoraDigitalObject object: cart){
	 System.out.println(object.getPid());
	  System.out.println(object.getDatastreams().get("IMG").getContent());%>
	 <img src="<%=object.getDatastreams().get("IMG").getContent()%>"></img>
	<%}%>

<form method="post" action="${pageContext.request.contextPath}/archives/downloads/checkout">
		<input type="submit" value="Checkout Items" name="checkout"/>
		</form>
</body>
</html>