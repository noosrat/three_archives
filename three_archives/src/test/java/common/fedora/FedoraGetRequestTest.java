package common.fedora;

import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class FedoraGetRequestTest {

	FedoraGetRequest instance;

	@Before
	public void setUp() throws Exception {
		instance = new FedoraGetRequest();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testResetRequest() {
		String expected = FedoraCredentials.getUrl() + "/objects";
		instance.resetRequest();
		assertEquals("The results did not match", expected, instance
				.getRequest().toString());
	}

	@Test
	public void testFindObjects() {
		TreeMap<QueryParameter, String> queryP = new TreeMap<QueryParameter, String>();
		queryP.put(QueryParameter.MAX_RESULTS, "10000");
		String expectedResult = FedoraCredentials.getUrl() 
				+ "/objects?pid=true&maxResults=10000&format=xml";
		instance.findObjects(queryP, DublinCore.PID);
		assertEquals("The results did not match", expectedResult, instance
				.getRequest().toString());

	}

	@Test
	public void testGetDatastreamDissemination() {
		TreeMap<QueryParameter, String> queryP = new TreeMap<QueryParameter, String>();
		instance = new FedoraGetRequest("test:01");
		queryP.put(QueryParameter.AS_OF_DATE_TIME, "2015-10-11 12:11:10");
		String expectedResult = FedoraCredentials.getUrl() 
				+ "/objects/test:01/datastreams/" + DatastreamID.AUD.name() + "/content?asOfDateTime=2015-10-11 12:11:10&format=xml";
		instance.getDatastreamDissemination(DatastreamID.AUD.name(),queryP);
		assertEquals("The results did not match", expectedResult, instance
				.getRequest().toString());
	}

	@Test
	public void testGetObjectHistory() {
		instance = new FedoraGetRequest("test:01");
		String expectedResult = FedoraCredentials.getUrl() 
				+ "/objects/test:01/versions?format=xml";
		instance.getObjectHistory();
		assertEquals("The results did not match", expectedResult, instance
				.getRequest().toString());

	}

	@Test
	public void testGetObjectProfile() {
		instance = new FedoraGetRequest("test:02");
		String expectedResult = FedoraCredentials.getUrl() 
				+ "/objects/test:02?format=xml";
		instance.getObjectProfile(new TreeMap<QueryParameter, String>());
		assertEquals("The results did not match", expectedResult, instance
				.getRequest().toString());

	}

	@Test
	public void testListDatastreams() {
		instance = new FedoraGetRequest("test:03");
		String expectedResult = FedoraCredentials.getUrl() 
				+ "/objects/test:03/datastreams?format=xml";
		instance.listDatastreams(new TreeMap<QueryParameter, String>());
		assertEquals("The results did not match", expectedResult, instance
				.getRequest().toString());

	}

	@Test
	public void testGetDatastreams() {
		instance = new FedoraGetRequest("test:04");
		String expectedResult = FedoraCredentials.getUrl() 
				+ "/objects/test:04/datastreams/" + DatastreamID.IMG.name() + "?validateChecksum=true&format=xml";
		TreeMap<QueryParameter, String> map = new TreeMap<QueryParameter, String>();
		map.put(QueryParameter.VALIDATE_CHECKSUM, "true");
		instance.getDatastream(DatastreamID.IMG.name(), map);
		assertEquals("The results did not match", expectedResult, instance
				.getRequest().toString());

	}

	@Test
	public void testGetDatastreamHistory() {

	}

	@Test
	public void testGetObjectXML() {

	}

}
