package exhibitions;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import exhibitions.ExhibitionService;

import org.junit.Before;
import org.junit.Test;

public class ExhibitionServiceTest {
	ExhibitionService instance;
	@Before
	public void setUp() throws Exception {
		
		instance = new ExhibitionService();
	}
	
	@Test
	public void testWhetherReturnToHomePageIfRequestNotValid() throws Exception {
		
		assertTrue(2==3);
	}

}
