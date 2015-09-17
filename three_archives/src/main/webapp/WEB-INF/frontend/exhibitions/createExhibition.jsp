<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<head>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/stylesheet.css"></link>
	
	<link rel="stylesheet" href="http://code.jquery.com/ui/1.11.4/themes/smoothness/jquery-ui.css">
	<script src="http://code.jquery.com/jquery-1.10.2.js"> </script>
	<script src="http://code.jquery.com/ui/1.11.4/jquery-ui.js"></script>
</head>
<body>
	<header>
		<h1>Create Exhibition</h1>
	</header>

	<form method="post" action="${pageContext.request.contextPath}/archives/create_exhibitions">
		<br>
		<h1>Select a template</h1>
		<ul id="templateSelection" style="list-style-type:none;margin:0;padding:0;margin:auto;">
			<li style="display:inline;float:left;font-weight:bold; padding:10px;">
				<input type="image" name="selectedTemplate" value="1" src="${pageContext.request.contextPath}/images/template1.jpg" alt="Template 1">
			</li>
			<li style="display:inline;float:left;font-weight:bold;padding:10px;">
				<input type="image" name="selectedTemplate" value="2" src="${pageContext.request.contextPath}/images/template2.jpg" alt="Template 2">
			</li>
			<li style="display:inline;float:left;font-weight:bold;padding:10px;">
				<input type="image" name="selectedTemplate" value="3" src="${pageContext.request.contextPath}/images/template3.jpg" alt="Template 3">
			</li>
		</ul>
		<br><br>
	</form>
</body>
