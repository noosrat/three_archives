package exhibitions;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import exhibitions.ExhibitionController;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


public class ExhibitionControllerTest {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private ExhibitionController instance;

	@Before
	public void setUp() throws Exception {
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		instance = new ExhibitionController();
	}
	
	@Test
	public void testWhetherReturnToHomePageIfRequestNotValid() throws Exception {
		when(request.getPathInfo()).thenReturn("/hello");
		String result = instance.execute(request, response);
		assertTrue(result.equalsIgnoreCase("index.jsp"));
	}
	
}
