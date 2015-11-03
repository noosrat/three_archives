package common.controller;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import history.HistoryController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maps.MapController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import search.BrowseController;
import search.SearchController;
import uploads.UploadController;
import User.UserController;
import downloads.DownloadController;
import exhibitions.ExhibitionController;

public class ServiceDelegatorTest {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private ServiceDelegator instance;

	@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		instance = new ServiceDelegator();
	}

	@Test
	public void testWhetherSearchControllerUsed() {
		when(request.getPathInfo()).thenReturn("/search.jsp");
		SearchController sc = Mockito.mock(SearchController.class);
		instance.getControllers().put("search", sc);
		String result = "";
		try {
			when(sc.execute(request, response)).thenReturn("../search.jsp");
			result = instance.execute(request, response);
			verify(sc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../search.jsp"));

	}

	@Test
	public void testWhetherBrowseControllerUsed() {
		when(request.getPathInfo()).thenReturn("/browse.jsp");
		BrowseController bc = Mockito.mock(BrowseController.class);
		instance.getControllers().put("browse", bc);
		String result = "";
		try {
			when(bc.execute(request, response)).thenReturn("../browse.jsp");
			result = instance.execute(request, response);
			verify(bc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../browse.jsp"));

	}

	@Test
	public void testWhetherExhibitionsControllerUsed() {
		when(request.getPathInfo()).thenReturn("/exhibitions.jsp");
		ExhibitionController ec = Mockito.mock(ExhibitionController.class);
		instance.getControllers().put("exhibitions", ec);
		String result = "";
		try {
			when(ec.execute(request, response)).thenReturn("../exhibitions.jsp");
			result = instance.execute(request, response);
			verify(ec).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../exhibitions.jsp"));

	}

	@Test
	public void testWhetherDownloadsControllerUsed() {
		when(request.getPathInfo()).thenReturn("/downloads.jsp");
	DownloadController dc= Mockito.mock(DownloadController.class);
		instance.getControllers().put("downloads", dc);
		String result = "";
		try {
			when(dc.execute(request, response)).thenReturn("../downloads.jsp");
			result = instance.execute(request, response);
			verify(dc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../downloads.jsp"));

	}
	
	@Test
	public void testWhetheMapsControllerUsed() {
		when(request.getPathInfo()).thenReturn("/maps.jsp");
		MapController mc = Mockito.mock(MapController.class);
		instance.getControllers().put("maps", mc);
		String result = "";
		try {
			when(mc.execute(request, response)).thenReturn("../maps.jsp");
			result = instance.execute(request, response);
			verify(mc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../maps.jsp"));

	}

	@Test
	public void testWhetherUploadsControllerUsed() {
		when(request.getPathInfo()).thenReturn("/uploads.jsp");
		UploadController uc = Mockito.mock(UploadController.class);
		instance.getControllers().put("uploads", uc);
		String result = "";
		try {
			when(uc.execute(request, response)).thenReturn("../uploads.jsp");
			result = instance.execute(request, response);
			verify(uc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../uploads.jsp"));

	}
	@Test
	public void testWhetherHistoryControllerUsed() {
		when(request.getPathInfo()).thenReturn("/history.jsp");
		HistoryController hc = Mockito.mock(HistoryController.class);
		instance.getControllers().put("history", hc);
		String result = "";
		try {
			when(hc.execute(request, response)).thenReturn("../history.jsp");
			result = instance.execute(request, response);
			verify(hc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../history.jsp"));

	}
	
	@Test
	public void testWhetherUserControllerUsed() {
		when(request.getPathInfo()).thenReturn("/user.jsp");
		UserController uc = Mockito.mock(UserController.class);
		instance.getControllers().put("user", uc);
		String result = "";
		try {
			when(uc.execute(request, response)).thenReturn("../user.jsp");
			result = instance.execute(request, response);
			verify(uc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../user.jsp"));

	}
	
	@Test
	public void testWhetherGeneralControllerUsed() {
		when(request.getPathInfo()).thenReturn("/hello.jsp");
		GeneralController gc = Mockito.mock(GeneralController.class);
		instance.getControllers().put("general", gc);
		String result = "";
		try {
			when(gc.execute(request, response)).thenReturn("../hello.jsp");
			result = instance.execute(request, response);
			verify(gc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
		assertTrue("The results did not match", result.equals("../hello.jsp"));

	}





}
