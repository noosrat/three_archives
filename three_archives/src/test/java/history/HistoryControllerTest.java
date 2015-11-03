package history;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class HistoryControllerTest {
	HistoryController instance;
	HttpServletRequest request;
	HttpServletResponse response;

	@Before
	public void setUp() throws Exception {
		instance = new HistoryController();
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testThatExecuteReturnedNullAndCalledInitialiseUserCookies() {
		when(request.getPathInfo()).thenReturn("/testingArchive");
		HttpSession session = Mockito.mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("ARCHIVE_CONCAT")).thenReturn(
				"testingArchive");
		try {
			String result = instance.execute(request, response);
			assertEquals(null, result);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("Test failed");
		}

	}

	@Test
	public void testWhetherHistoryHomePageReturnedAsAResultOfHistoryInPAthInfo() {
		when(request.getPathInfo()).thenReturn("/");
		HttpSession session = Mockito.mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("ARCHIVE_CONCAT")).thenReturn(
				"testingArchive");
		//cookies
		when(session.getAttribute("dateLastVisited")).thenReturn("test date");
		when(session.getAttribute("browseCategoryCookie")).thenReturn("test browseCatgegorycookie");
		when(session.getAttribute("objectsModifiedSinceLastVisit")).thenReturn("test objects");
		when(session.getAttribute("categoriesWithRecentUpdates")).thenReturn("test categories");
		when(session.getAttribute("userFavouriteCategoriesWithRecentUpdates")).thenReturn("test categories user");
		when(session.getAttribute("tagCloud")).thenReturn("test tag cloud");
		//
		try {
			String result = instance.execute(request, response);
			assertEquals("WEB-INF/frontend/historyandstatistics/history.jsp", result);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("test failed");
		}
	}
	
	@Test
	public void testWhetherHistoryHomePageReturnedAsAResultBrowsePathInfo() {
		when(request.getPathInfo()).thenReturn("/browse");
		HttpSession session = Mockito.mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("ARCHIVE_CONCAT")).thenReturn(
				"testingArchive");
		//cookies
		when(session.getAttribute("dateLastVisited")).thenReturn("test date");
		when(session.getAttribute("browseCategoryCookie")).thenReturn("test browseCatgegorycookie");
		when(session.getAttribute("objectsModifiedSinceLastVisit")).thenReturn("test objects");
		when(session.getAttribute("categoriesWithRecentUpdates")).thenReturn("test categories");
		when(session.getAttribute("userFavouriteCategoriesWithRecentUpdates")).thenReturn("test categories user");
		when(session.getAttribute("tagCloud")).thenReturn("test tag cloud");
		//
		try {
			String result = instance.execute(request, response);
			assertEquals("WEB-INF/frontend/historyandstatistics/history.jsp", result);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("test failed");
		}
	}
	

	@Test
	public void testWhetherHistoryHomePageReturnedAsAResultOfNoPathInfo() {
		when(request.getPathInfo()).thenReturn("/");
		HttpSession session = Mockito.mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		when(session.getAttribute("ARCHIVE_CONCAT")).thenReturn(
				"testingArchive");
		//cookies
		when(session.getAttribute("dateLastVisited")).thenReturn("test date");
		when(session.getAttribute("browseCategoryCookie")).thenReturn("test browseCatgegorycookie");
		when(session.getAttribute("objectsModifiedSinceLastVisit")).thenReturn("test objects");
		when(session.getAttribute("categoriesWithRecentUpdates")).thenReturn("test categories");
		when(session.getAttribute("userFavouriteCategoriesWithRecentUpdates")).thenReturn("test categories user");
		when(session.getAttribute("tagCloud")).thenReturn("test tag cloud");
		//
		try {
			String result = instance.execute(request, response);
			assertEquals("WEB-INF/frontend/historyandstatistics/history.jsp", result);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new AssertionError("test failed");
		}
	}
}
