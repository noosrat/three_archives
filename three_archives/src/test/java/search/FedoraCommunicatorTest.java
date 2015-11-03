package search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import common.fedora.Datastream;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraClient;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraGetRequest;
import common.fedora.State;

//remember that we are not trying to test fedora code..we are trying to test OUR code
public class FedoraCommunicatorTest {

	/**
	 * To test: 1. Populate Fedora Digital Object 2. Find fedora digital object
	 */
	private FedoraCommunicator instance;

	@Before
	public void setUp() throws Exception {
		instance = new FedoraCommunicator();
		FedoraClient fedoraClient = Mockito.mock(FedoraClient.class);
	}

	private Set<FedoraDigitalObject> getExpectedResult() {
		Set<FedoraDigitalObject> objects = new HashSet<FedoraDigitalObject>();
		for (int x = 0; x < 5; x++) {
			objects.add(new FedoraDigitalObject("test:" + (Math.random() + 1)));
		}
		return objects;
	}

	private ArrayList<String> getPids(
			Set<FedoraDigitalObject> fedoraDigitalObjects) {
		ArrayList<String> result = new ArrayList<String>();
		for (FedoraDigitalObject object : fedoraDigitalObjects) {
			result.add(object.getPid());
		}
		return result;
	}

	@Test
	public void testFindFedoraDigitalObjectsWithSearchFeature() {
		// Set<FedoraDigitalObject> expectedResult = getExpectedResult();
		// Method method;
		// try {
		// method = instance.getClass().getDeclaredMethod(
		// "findAndPopulateFedoraDigitalObjects", String.class, String.class);
		// } catch (NoSuchMethodException e1) {
		// throw new AssertionError("Method not found " + e1.getMessage());
		// }
		// method.setAccessible(true);
		//
		// SolrCommunicator sc = Mockito.mock(SolrCommunicator.class);
		// FedoraClient fc = Mockito.mock(FedoraClient.class);
		// FedoraXMLResponseParser fxml =
		// Mockito.mock(FedoraXMLResponseParser.class);
		// try{
		// when(sc.solrSearch("test")).thenReturn(getPids(expectedResult));
		// when(fc.ex)
		// method.invoke(instance, "test","search");
		// for (FedoraDigitalObject ob: expectedResult){
		//
		// }
		//
		// Set<FedoraDigitalObject> actualResult;
		// }
		// catch(Exception ex){
		//
		// }
		// try {
		// when(method.invoke(instance, "", "")).thenReturn(expectedResult);
		// method.invoke(instance, "", "");
		// actualResult = instance.findFedoraDigitalObjects("", "");
		// } catch (Exception e) {
		// throw new AssertionError("Test failed " + e.getMessage());
		// }
		//
		// boolean pass = true;
		//
		// for (FedoraDigitalObject object : expectedResult) {
		// pass = actualResult.contains(object);
		// if (!pass) {
		// throw new AssertionError("Test failed.  Items did not match");
		// }
		// }
	}

	private FedoraDigitalObject expectedResult() {
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(
				"test:" + Math.random() + 1);
		fedoraDigitalObject.setState(State.A);
		fedoraDigitalObject.setDateCreated(new Date());
		fedoraDigitalObject.setDateLastModified(new Date());
		fedoraDigitalObject.setDatastreams(getDatastreams(fedoraDigitalObject
				.getPid()));
		fedoraDigitalObject.setXmlRepresentation(null);
		fedoraDigitalObject.setVersionHistory(new ArrayList(Arrays.asList(
				new Date().toString(), new Date(1000).toString(), new Date(
						12345678).toString())));
		return fedoraDigitalObject;
	}

	private HashMap<String, Datastream> getDatastreams(String pid) {

		HashMap<String, Datastream> datastreams = new HashMap<String, Datastream>();
		Datastream img = new Datastream(pid, DatastreamID.IMG);
		int random = (int) Math.random() + 1;
		img.setLocation("documents/images/img" + random + ".jpg");
		datastreams.put(DatastreamID.IMG.name(), new Datastream(pid,
				DatastreamID.IMG));
		HashMap<String, String> meta = new HashMap<String, String>();
		meta.put(DublinCore.TITLE.name(), "test " + random);
		DublinCoreDatastream dc = new DublinCoreDatastream(pid, meta);
		datastreams.put(DatastreamID.DC.name(), dc);
		return datastreams;
	}

	@Test
	public void testPopulateFedoraDigitalObject() {
		
	}
	// //findFedoraObjectsUsingSearchTerms
	// GetDatastreamResponse getDatastreamResponse =
	// Mockito.mock(GetDatastreamResponse.class);
	// when(getDatastreamResponse.getDatastreamProfile()).thenReturn(value)
	// for (String p: pids){
	// when(fedoraClient.getDatastream(p,
	// "img").execute()).thenReturn(getDatastreamResponse);
	// }
	//
	//
	//
	// }
	//
	// // @Test
	// public void testFindFedoraObjects(){
	//
	// ArrayList<DatastreamProfile> result = new ArrayList<DatastreamProfile>();
	// ArrayList<DatastreamProfile> expectedResult = expectedResult();
	// FedoraClient fedoraClient = Mockito.mock(FedoraClient.class);
	// instance.setFedoraClient(fedoraClient);
	//
	//
	// Method method;
	// try {
	// method =
	// instance.getClass().getDeclaredMethod("getFedoraObjectsImageDatastream",
	// String.class);
	// } catch (NoSuchMethodException e1) {
	// throw new AssertionError("Method not found " + e1.getMessage());
	// }
	// method.setAccessible(true);
	// try {
	// method.invoke(instance, "");
	// // when(method.invoke(instance, "")).thenReturn(expectedResult());
	// result = instance.findFedoraObjects("");
	// } catch (Exception e) {
	// throw new AssertionError("Test failed " + e.getMessage());
	// }
	//
	// for (int x=0; x<result.size(); x++){
	// assertTrue("PIDs do not match",
	// result.get(x).getPid().equals(expectedResult.get(x).getPid()));
	// }
	//

	// }
}
