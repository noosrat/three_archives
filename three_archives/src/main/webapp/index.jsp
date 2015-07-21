<%@page import="java.util.List"%>
<%@page
	import="com.yourmediashelf.fedora.generated.management.DatastreamProfile"%>

<html>
<body>
	<h2>Three Archives Search</h2>



	<form action="archives/search" name="search" method="post">
		<input type="submit" value="Search for Fedora objects" /> <input
			type="text" name="terms">
	</form>
	<%
		List<DatastreamProfile> result = (List<DatastreamProfile>) request.getAttribute("objects");
		if (result != null) {
	%>
	The number of objects found is
	<%=result.size()%>
	<br>
	<br>
	<table style="">
		<%
			int i = 0;
				for (DatastreamProfile datastreamProfile : result) {
					i++;
					if (i % 5 == 0) {
		%>
		<tr>
			<%
				}
			%>
			<!-- <td><a href="<%=datastreamProfile.getDsLocation()%>"><%=datastreamProfile.getPid()%></a></td> 
			<td><%=datastreamProfile.getDsLabel()%></td> -->
			<td><img src="<%=datastreamProfile.getDsLocation()%>"style="width: 304px; height: 228px;"></td>
			<%
				if (i % 4 == 0) {
			%>
		</tr>

		<%
			}
				}
		%>
	</table>
	<%
		}
	%>


</body>
</html>
