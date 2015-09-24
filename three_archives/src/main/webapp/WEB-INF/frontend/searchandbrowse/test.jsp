<html>
<head>
	<title> Quick simple lightbox </title>
	<link href="${pageContext.request.contextPath}/css/bootstrap.css" rel="stylesheet">
	<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/search_and_browse.css"></link>
	<link href="${pageContext.request.contextPath}/css/lightbox.css" rel="stylesheet">
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap-lightbox.min.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/prettify.css">
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>



	<div class="container" class="gallery">
			<div class="row">
				<div class="col-lg-3">	
				<a href="${pageContext.request.contextPath}/images/flower.jpg" data-title="My first caption" data-lightbox="garden">
					<img src="${pageContext.request.contextPath}/images/flower.jpg" width="200px" class="img-thumbnail"/>
				</a>
				</div>	
				<div class="col-lg-3">	
				<a href="${pageContext.request.contextPath}/images/flower2.jpg" data-title="My second caption" data-lightbox="garden">
					<img src="${pageContext.request.contextPath}/images/flower2.jpg" width="200px" class="img-thumbnail"/>
				</a>
				</div>	
				<div class="col-lg-3">	
				<a href="${pageContext.request.contextPath}/images/flower3.jpg" data-title="My third caption" data-lightbox="garden">
					<img src="${pageContext.request.contextPath}/images/flower3.jpg" width="200px" class="img-thumbnail"/>
				</a>
				</div>	
				<div class="col-lg-3">	
				<a href="${pageContext.request.contextPath}/images/flower4.jpg" data-title="My fourth caption" data-lightbox="garden">
					<img src="${pageContext.request.contextPath}/images/flower4.jpg" width="200px" class="img-thumbnail"/>
				</a>
				</div>			
			</div>
		</div>
	
	
	<div>
		<ul class="thumbnails">
			<li class="span2">
				<a data-toggle="lightbox" href="#demoLightbox" class="thumbnail" data-target="#demoLightbox">
					<img src="${pageContext.request.contextPath}/images/flower2.jpg" alt="Click to view the lightbox">
				</a>
			</li>
		</ul>
		<div id="demoLightbox" class="lightbox hide fade"  tabindex="-1" role="dialog" aria-hidden="true">
			<div class='lightbox-content'>
				<img src="${pageContext.request.contextPath}/images/flower2.jpg">
				<div class="lightbox-caption"><p>Your caption here</p></div>
			</div>
		</div>
	</div>
		

	<script src="${pageContext.request.contextPath}/jquery.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
	<script src="${pageContext.request.contextPath}/js/prettify.js"></script>
	<script src="${pageContext.request.contextPath}/js/bootstrap-lightbox.min.js"></script>	
	<script>
	$(document).ready(function()
	{
		prettyPrint();
	});
	</script>
	<script type="text/javascript">
		var _gaq = _gaq || [];
		_gaq.push(['_setAccount', 'UA-38085002-1']);
		_gaq.push(['_trackPageview']);

		(function() {
		 var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
		 ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
		 var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
		 })();
	</script>
</body>

</html>