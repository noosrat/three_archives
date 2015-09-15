package common.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maps.MapController;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import search.SearchController;
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
	public void testWhetherReturnToHomePageSincePathDoesNotMatchAnyController() {
		when(request.getPathInfo()).thenReturn("/hello");
		String result = instance.execute(request, response);
		assertTrue(result.equalsIgnoreCase("index.jsp"));
	}

	@Test
	public void testWhetherSearchControllerUsed() {
		when(request.getPathInfo()).thenReturn("/search.jsp");
		SearchController sc = Mockito.mock(SearchController.class);
		instance.getControllers().put("search", sc);
		try {
			instance.execute(request, response);
			verify(sc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
	}

	@Test
	public void testWhetherMapsControllerUsed() {
		when(request.getPathInfo()).thenReturn("/maps");
		MapController mc = Mockito.mock(MapController.class);
		instance.getControllers().put("maps", mc);
		try {
			instance.execute(request, response);
			verify(mc).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
	}

	@Test
	public void testWhetherExhibitionsControllerUsed() {
		when(request.getPathInfo()).thenReturn("/exhibitions");
		ExhibitionController ec = Mockito.mock(ExhibitionController.class);
		instance.getControllers().put("exhibitions", ec);
		try {
			instance.execute(request, response);
			verify(ec).execute(request, response);
		} catch (Exception e) {
			throw new AssertionError("test failed " + e.getMessage());
		}
	}

}
