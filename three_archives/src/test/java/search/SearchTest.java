package search;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;
import common.Service;

public class SearchTest {

	private Search instance;
	private FedoraCommunicator fedoraCommunicator;
	
	@Before
	public void setUp() throws Exception {
		instance = new Search();
		fedoraCommunicator = Mockito.mock(FedoraCommunicator.class);
		try{
			Field communicator = Service.class.getDeclaredField("communicator");
			communicator.setAccessible(true);
			communicator.set(instance, fedoraCommunicator);
			
		}catch(Exception ex){
			throw new AssertionError("Test initialisation failed, could not find field " + ex.getMessage());
		}
	}
	
	@After
	public void tearDown(){
		instance = null;
		fedoraCommunicator = null;
	}

	private ArrayList<DatastreamProfile> expectedResult() {
		ArrayList<DatastreamProfile> result = new ArrayList<DatastreamProfile>();
		for (int x = 0; x < 5; x++) {
			DatastreamProfile d1 = new DatastreamProfile();
			d1.setPid("test:" + Math.random());

			result.add(d1);
		}
		return result;

	}

	@Test
	public void testFindObjects(){
		ArrayList<DatastreamProfile> expectedResult = expectedResult();
		List<DatastreamProfile> result = new ArrayList<DatastreamProfile>();
		
		try{
			when(fedoraCommunicator.findFedoraObjects(null)).thenReturn(expectedResult);
			result = instance.findObjects(null);
		} catch (FedoraClientException e) {
			throw new AssertionError("test failed" + e.getMessage());
		}
		
		for (int x=0; x<result.size(); x++){
			assertTrue("PIDs do not match", result.get(x).getPid().equals(expectedResult.get(x).getPid()));
		}

		
	}
	
	@Test
	public void testFindObjectsWithQuery(){
		ArrayList<DatastreamProfile> expectedResult = expectedResult();
		List<DatastreamProfile> result = new ArrayList<DatastreamProfile>();
		
		try {
			when(fedoraCommunicator.findFedoraObjects(null)).thenReturn(expectedResult);
			result = instance.findObjectsWithQuery(null);
		} catch (FedoraClientException e) {
			throw new AssertionError("test failed" + e.getMessage());
		}
		for (int x=0; x<result.size(); x++){
			assertTrue("PIDs do not match", result.get(x).getPid().equals(expectedResult.get(x).getPid()));
		}
	}
	
}
