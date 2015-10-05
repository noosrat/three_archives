<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/css/annotorious.css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/annotorious.min.js"></script>
<script type="text/javascript">
window.onload = function () {
    anno.addAnnotation({
        src: "http://localhost:8080/fedora/objects/ms:1/datastreams/IMG/content",
        text: "This annotation will appear",
        shapes: [{
            type: 'rect',
            geometry: {
                x: 0.1,
                y: 0.1,
                width: 0.4,
                height: 0.3
            }
        }]
    });
}
</script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<img class="annotatable" src="http://annotorious.github.io/demos/640px-Hallstatt_300.jpg" data-original="id-of-image">
<div>
    <button onclick="addAnnotationByDataOriginal()">Add Annotation by Data-Original Attribute (won't work)</button>
</div>
<br/>
<img class="annotatable" src="http://localhost:8080/fedora/objects/ms:1/datastreams/IMG/content">
<div>
    <button onclick="addAnnotationByImgUrl()">Add Annotation by Url (will work)</button>
</div>
</body>
<script type="text/javascript">
addAnnotationByImgUrl();
</script>
</html>