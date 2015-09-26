<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
  <title>View exhibition</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="${pageContext.request.contextPath}/stylish-portfolio.css" rel="stylesheet">
  <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
  <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
  <link type="text/css" href="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.css" rel="stylesheet" />
  <script src="${pageContext.request.contextPath}/jquery.js"></script>
  <script src="${pageContext.request.contextPath}/jquery.bxslider/jquery.bxslider.min.js"></script>
    
    
<script>
    // Closes the sidebar menu
    $("#menu-close").click(function(e) {
        e.preventDefault();
        $("#sidebar-wrapper").toggleClass("active");
    });

    // Opens the sidebar menu
    $("#menu-toggle").click(function(e) {
        e.preventDefault();
        $("#sidebar-wrapper").toggleClass("active");
    });

    // Scrolls to the selected menu item on the page
    $(function() {
        $('a[href*=#]:not([href=#])').click(function() {
            if (location.pathname.replace(/^\//, '') == this.pathname.replace(/^\//, '') || location.hostname == this.hostname) {

                var target = $(this.hash);
                target = target.length ? target : $('[name=' + this.hash.slice(1) + ']');
                if (target.length) {
                    $('html,body').animate({
                        scrollTop: target.offset().top
                    }, 1000);
                    return false;
                }
            }
        });
    });
    

</script>	
	
	<script>
		$(document).ready(function(){
 		 $('.slider4').bxSlider({
   		 slideWidth: 210,
   		 minSlides: 2,
   	 	 maxSlides: 4,
   		 moveSlides: 2,
   	 	slideMargin: 8,
   	 	infiniteLoop:false,
		hideControlOnEnd:true
  		});
		});
	</script>
 <style>
		.header{
	 background: url(${pageContext.request.contextPath}/images/Back.jpg);
}
		.droptargetCart {
 			margin: 5px;
    		padding: 5px;
   		}
		.droptargetTemplate {
   			width: 210px; 
    			height: 210px;
    			border: 1px dotted #aaaaaa;
    	}
		.nodroptargetTemplate {
    		width: 100px; 
    		height: 200px;
    		margin: 15px;
    		padding: 10px;
    	}
		.slide{
			height:570px;
		}
		.caption{
			margin:2px;
		}
    	

		
	</style>

</head>
<body>
	<header id="top" class="header">
       <div class="text-vertical-center">
            <h1>"<%out.println(request.getAttribute("ExhibitionTitle")); %>"</h1>
            <h4><%out.println(request.getAttribute("ExhibitionDescription")); %></h4>
  			<p>Created by: <%out.println(request.getAttribute("ExhibitionCreator")); %></p>          
			<br>
            <a href="#about" class="btn btn-dark btn-lg">View exhibition</a>
       </div>
	</header>


	<%String[] images =(String [])request.getAttribute("PopulatedTemplateArray"); %>
	<%String[] captions =(String [])session.getAttribute("CAPTIONS"); %>
	<section id="about" >  
		<div class="container">
            <div class="row">
                <div class="col-lg-12 text-center">
                    <h2><%out.println(request.getAttribute("ExhibitionTitle")); %></h2>  	
  					<div class="slider4" >
  					<div class="slide">
						<img src="<%=images[0]%>">		
						<textarea class="TextArea" rows="2" cols="26"  name="input_cap1" readonly=readonly><%=captions[1] %></textarea>
					</div>
					<div class="slide">	
						<textarea class="TextArea" rows="2" cols="26"  name="input_cap2" readonly=readonly><%=captions[2] %></textarea>
						<img src="<%=images[3]%>">	
						
					</div>
					<div class="slide">
						<img src="<%=images[4]%>">	
							
						<textarea class="TextArea" rows="2" cols="26"  name="input_cap5" readonly=readonly><%=captions[5] %></textarea>	
					</div>	
					<div class="slide">
							
						<textarea class="TextArea" rows="2" cols="26"  name="input_cap6" readonly=readonly><%=captions[6] %></textarea>
						<img src="<%=images[7]%>">	
							
					</div>
					<div class="slide">
						<img src="<%=images[8]%>">	
						
							
						<textarea class="TextArea" rows="2" cols="26"  name="input_cap9" readonly=readonly><%=captions[9] %></textarea>
					</div>
					<div class="slide">
						
						<textarea class="TextArea" rows="2" cols="26"  name="input_cap10" readonly=readonly><%=captions[10] %></textarea>
						<img src="<%=images[11]%>">	
						
					</div>
					</div>
				</div>
       		</div>
		</div>
	</section>
	

 
</body>
</html>