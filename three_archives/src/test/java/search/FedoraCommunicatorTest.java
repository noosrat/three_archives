package search;


//remember that we are not trying to test fedora code..we are trying to test OUR code
public class FedoraCommunicatorTest {

	
	/**
	 * To test:
	 * 1.  Populate Fedora Digital Object
	 * 2.  Find fedora digital object
	 */
//	private FedoraCommunicator instance;
//	private FedoraClient fedoraClient;
//
//	@Before
//	public void setUp() throws Exception {
//		instance = new FedoraCommunicator();
//		FedoraClient fedoraClient = Mockito.mock(FedoraClient.class);
//		instance.setFedoraClient(fedoraClient);
//	}
//
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
//	public void testFindFedoraObjects(){
//		ArrayList<DatastreamProfile> expectedResult = expectedResult();
//		List<String> pids = new ArrayList<String>();
//		for (DatastreamProfile d: expectedResult){
//			pids.add(d.getPid());
//		}
//	//this method calls the getFedoraObjectsImageDataStream method
//		
//		//the getFedoraObjectsImageDataStream calls the findFedoraObjectsUsingSearchTerms method
//		//search terms empty or not empty
//		//findFedoraObjectsUsingSearchTerms
//
//		try {
//			FindObjectsResponse response = Mockito.mock(FindObjectsResponse.class);
//			when(response.getPids()).thenReturn(pids);
//			when(fedoraClient.findObjects().terms("*").pid().maxResults(10000).execute()).thenReturn(response);
//		
//		} catch (FedoraClientException e) {
//			throw new AssertionError("could not findFedoraObjectsUsingSearchTerms" + e.getMessage());
//		}
//		//findFedoraObjectsUsingSearchTerms
//		GetDatastreamResponse getDatastreamResponse = Mockito.mock(GetDatastreamResponse.class);
//		when(getDatastreamResponse.getDatastreamProfile()).thenReturn(value)
//		for (String p: pids){
//		when(fedoraClient.getDatastream(p, "img").execute()).thenReturn(getDatastreamResponse);
//		}
//		
//		
//	
//	}
//
////	@Test
//	public void testFindFedoraObjects(){
//		
//		ArrayList<DatastreamProfile> result = new ArrayList<DatastreamProfile>();
//		ArrayList<DatastreamProfile> expectedResult = expectedResult();
//		FedoraClient fedoraClient = Mockito.mock(FedoraClient.class);
//		instance.setFedoraClient(fedoraClient);
//		
//		
//		Method method;
//		try {
//			method = instance.getClass().getDeclaredMethod("getFedoraObjectsImageDatastream", String.class);
//		} catch (NoSuchMethodException e1) {
//		throw new AssertionError("Method not found " + e1.getMessage());
//		}
//		method.setAccessible(true);
//		try {
//		method.invoke(instance, "");
////			when(method.invoke(instance, "")).thenReturn(expectedResult());
//			result = instance.findFedoraObjects("");
//		} catch (Exception e) {
//			throw new AssertionError("Test failed " + e.getMessage());
//		}		
//		
//		for (int x=0; x<result.size(); x++){
//			assertTrue("PIDs do not match", result.get(x).getPid().equals(expectedResult.get(x).getPid()));
//		}
//		
	
//}
}
