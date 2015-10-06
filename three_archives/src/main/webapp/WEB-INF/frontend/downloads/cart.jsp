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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
<title>Downloads</title>
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
	alert(thing);
	//console.log(('#ms:1').value);
	remove.push(thing);}
else
	{
	console.log($(this).value);
	remove.splice(remove.indexOf($(this).attr("value")), 1)}

document.getElementById("demo").innerHTML = remove;
console.log(remove);
}
</script>
<!-- style="display:none;" -->

<!-- <form method="post" action="${pageContext.request.contextPath}/archives/downloads/remove"> -->
<%Set<FedoraDigitalObject> cart = (HashSet<FedoraDigitalObject>) request.getAttribute("cart");

for(FedoraDigitalObject object: cart){
	 System.out.println(object.getPid());
	  System.out.println(object.getDatastreams().get("IMG").getContent());%>
	 
	 
	 	<input id="<%=object.getPid()%>" type="checkbox" name="item" data-pid="<%=object.getPid()%>" onchange = "change(this)" checked ><img src="<%=object.getDatastreams().get("IMG").getContent()%>" height="42" width="42"></img><br>
	
	<%}%>
	
<!-- http://localhost:8080/fedora/objects/ms:1/datastreams/IMG/content?download=true&format=xml -->
<form method="post" action="${pageContext.request.contextPath}/archives/downloads/checkout">
		<textarea id="demo" name="deletions" readonly=readonly></textarea>
		<input type="submit" value="Checkout Items" name="checkout"/>
		</form>
		
</body>
</html>