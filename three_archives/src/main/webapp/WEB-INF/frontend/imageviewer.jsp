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
<script type="text/javascript">
jQuery.noConflict();

jQuery(document).ready(function($) {
    // You can use the locally-scoped $ in here as an alias to jQuery.
    $('div').hide();
});
</script>
<!--<% String pic = request.getParameter("image"); %>-->
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/annotorious.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/annotorious.min.js"></script>
<script language="JavaScript" type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.js"></script>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Movie Snaps | Image View</title>
</head>
<body>
<script type="text/javascript">
<%FedoraDigitalObject ob = (FedoraDigitalObject) request.getAttribute("view");%>

//var cart='<%=request.getSession().getAttribute("MEDIA_CART")%>';
//cart.add(ob.getPid());
var pid='<%=ob.getPid()%>';
var selected="";
//cart.push(pid);
////
console.log(<%=request.getParameter("addedtocart")%>);
function sendToCart(){
	selected=selected.concat(pid, ">");
	document.getElementById("cartItems").innerHTML = selected;
	console.log(selected);
	
}

	window.onload = function () {
	<%String[] annotations=(((DublinCoreDatastream)ob.getDatastreams().get("DC")).getAnnotations()).split(";");
	for (int m=0; m<annotations.length; m++){
	String[] anna=annotations[m].split("/");%>
	anno.addAnnotation({
        src: "<%=ob.getDatastreams().get("IMG").getContent()%>",
        text: '<%=anna[0]%>',//((DublinCoreDatastream)ob.getDatastreams().get("DC")).getAnnotations()%>",
        editable : false,
        shapes: [{
            type: 'rect',
            geometry: {
                x: <%=anna[1]%>,//0.1,
                y: <%=anna[2]%>,
                width: <%=anna[3]%>,
                height: <%=anna[4]%>
            }
        }]
    });
	<%}%>
}
var annotations="";//move global
function see(){
	var ann=anno.getAnnotations("<%=ob.getDatastreams().get("IMG").getContent()%>");
	
	
	for (var k=0; k<ann.length; k++){
		var text=ann[k]["text"];
		var x=ann[k]["shapes"][0]["geometry"]["x"];
		var y=ann[k]["shapes"][0]["geometry"]["y"];
		var w=ann[k]["shapes"][0]["geometry"]["width"];
		var h=ann[k]["shapes"][0]["geometry"]["height"];
		annotations=text+"/"+x+"/"+y+"/"+w+"/"+h+";";
	}
	console.log(ann[1]["text"]);
	console.log(ann[1]["shapes"][0]["geometry"]);
	document.getElementById("demo").innerHTML = annotations;	
}


	<%System.out.println("Description:"+ ((DublinCoreDatastream) ob.getDatastreams().get("DC")).getAnnotations());%>
	<%System.out.println("Title:"+ ((DublinCoreDatastream) ob.getDatastreams().get("DC")).getTitle());%>
	//System.out.println("addanno");
		//System.out.print(((DublinCoreDatastream) ob.getDatastreams().get("DC")).getAnnotations());
	//anno.AddAnnotation(myAnnotation);
	//System.out.println("added anno");%>
	//var pid = '//%=ob.getPid()%>'
</script>
<h1>Name</h1>
<!--<img src="<%= pic %>">-->
<img src="<%=ob.getDatastreams().get("IMG").getContent()%>" id=myImage class="annotatable"/>

<h4>Details</h4>



<form name="map" method="post" action="${pageContext.request.contextPath}/archives/redirect_maps/place?image=<%=ob.getPid()%>">
<!-- place word map in url-->
    <input type="submit" value="Place Me" />
</form>

<form action="${pageContext.request.contextPath}/archives/redirect_maps/">
<textarea id="demo" name="annotations" readonly=readonly style="display:none;"></textarea>
<input type="submit" onclick="see()" value="Save Comments"/>
</form>
<form action="${pageContext.request.contextPath}/archives/redirect_maps/">
<textarea id="cartItems" name="addedtocart" readonly=readonly style="display:none;"></textarea>
<input type="submit" onclick="sendToCart()" value="Send To Cart"/>
</form>
</body>

</html>