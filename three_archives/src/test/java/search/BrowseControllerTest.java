package search;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class BrowseControllerTest {
//difficult to test since just passing control to different places but going to same jsp
	BrowseController instance;
	@Before
	public void setUp() throws Exception {
		instance = new BrowseController();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWhetherDownloadsCalled() {
	}
	
	

}
