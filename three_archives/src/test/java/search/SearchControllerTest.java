package search;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchControllerTest {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private SearchController instance;
	private Search searchMock;

//	@Before
//	public void setUp() throws Exception {
//		request = Mockito.mock(HttpServletRequest.class);
//		response = Mockito.mock(HttpServletResponse.class);
//		instance = new SearchController();
//
//		searchMock = Mockito.mock(Search.class);
//
//		try {
//			Field search = SearchController.class.getDeclaredField("search");
//			search.setAccessible(true);
//			search.set(instance, searchMock);
//		} catch (Exception e1) {
//			throw new AssertionError(
//					"Test initialisation failed.  Could not set field "
//							+ e1.getMessage());
//
//		}
//
//	}
//
//	@Test
//	public void testHttpRequestDoesNotContainActionFromForm() {
//		when(request.getPathInfo()).thenReturn("/");
//		
//		String result;
//		try {
//	
//			result = instance.execute(request, response);
//		} catch (FedoraClientException e) {
//			throw new AssertionError("Test failed " + e.getMessage());
//		}
//		assertTrue(result
//				.equalsIgnoreCase("WEB-INF/frontend/searchandbrowse/search_home.jsp"));
//	}
//
//	@Test
//	public void testHttpRequestRedirectsToSearchHome() {
//		when(request.getPathInfo()).thenReturn("/redirect_search");
//		String result;
//		try {
//			result = instance.execute(request, response);
//		} catch (FedoraClientException e) {
//			throw new AssertionError("Test failed " + e.getMessage());
//		}
//		assertTrue(result
//				.equalsIgnoreCase("WEB-INF/frontend/searchandbrowse/search_home.jsp"));
//
//	}
//
//
//	@Test
//	public void testHttpRequestExecuteSearchFedoraObjectsWithEmptyInputsAndResults() {
//		when(request.getPathInfo()).thenReturn("/search_objects");
//
//		when(request.getParameter("terms")).thenReturn(null);
//		when(request.getParameter("query")).thenReturn(SearchController.Query.ALL.toString());
//		
//		String result;
//		try {
//			when(searchMock.findObjects(null)).thenReturn(null);
//			result = instance.execute(request, response);
//			verify(request).setAttribute("message", "Please enter a search term");
//		} catch (FedoraClientException e) {
//			throw new AssertionError("Test failed " + e.getMessage());
//		}
//
//		assertTrue(result
//				.equalsIgnoreCase("WEB-INF/frontend/searchandbrowse/search_home.jsp"));
//
//	}
//
//	private ArrayList<DatastreamProfile> expectedResult() {
//		ArrayList<DatastreamProfile> result = new ArrayList<DatastreamProfile>();
//		for (int x = 0; x < 5; x++) {
//			DatastreamProfile d1 = new DatastreamProfile();
//			d1.setPid("test:" + Math.random());
//
//			result.add(d1);
//		}
//		return result;
//
//	}
//	@Test
//	public void testHttpRequestExecuteSearchFedoraObjectsWithNonEmptyResults() {
//		when(request.getPathInfo()).thenReturn("/search_objects");
//		ArrayList<DatastreamProfile> output = expectedResult();
//		
//		when(request.getParameter("terms")).thenReturn("image");
//		when(request.getParameter("query")).thenReturn(SearchController.Query.ALL.toString());
//		
//		String result;
//		try {
//			when(searchMock.findObjects("image")).thenReturn(output);
//			result = instance.execute(request, response);
//			verify(request).setAttribute("objects", output);
//		} catch (FedoraClientException e) {
//			throw new AssertionError("Test failed " + e.getMessage());
//		}
//
//		assertTrue(result
//				.equalsIgnoreCase("WEB-INF/frontend/searchandbrowse/search_home.jsp"));
//
//	}

}
