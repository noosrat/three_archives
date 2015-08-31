package search;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.request.FedoraRequest;
import com.yourmediashelf.fedora.client.response.FindObjectsResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamResponse;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

public class FedoraCommunicator {

	private static FedoraClient fedora;

	public FedoraCommunicator() {
		try {
			FedoraCredentials credentials = new FedoraCredentials("http://localhost:8080/fedora", "fedoraAdmin",
					"fedoraAdmin");
			FedoraCommunicator.fedora = new FedoraClient(credentials);

			FedoraRequest.setDefaultClient(fedora);

		} catch (MalformedURLException ex) {
			System.out.println("error, credentials incorrect, malformed" + ex);
		}
	}

	public void setFedoraClient(FedoraClient fedoraClient) {
		FedoraCommunicator.fedora = fedoraClient;
	}

	public FedoraClient getFedora() {
		return fedora;
	}

	public ArrayList<DatastreamProfile> findFedoraObjects(String terms) throws FedoraClientException {
		return getFedoraObjectsImageDatastream(terms);
	}
	
	public ArrayList<DatastreamProfile> findFedoraObjectsWithQuery(String query) throws FedoraClientException {
		return getFedoraObjectsImageDatastreamWithQuery(query);
	}
	
	

	private List<String> findFedoraObjectsUsingSearchTerms(String terms) throws FedoraClientException {
		FindObjectsResponse findObjectsResponse = null;
		if (terms == null || terms.equals("")) {
			findObjectsResponse = FedoraClient.findObjects().terms("*").pid().maxResults(10000).execute();
		} else {
			findObjectsResponse = FedoraClient.findObjects().terms("*"+terms+"*").pid().maxResults(10000).execute();
		}
		return findObjectsResponse.getPids();
	}

	private List<String> findFedoraObjectsQuery(String query) throws FedoraClientException {
		FindObjectsResponse findObjectsResponse = null;
		findObjectsResponse = FedoraClient.findObjects().query(query).pid().maxResults(10000).execute();
		return findObjectsResponse.getPids();
	}

	private ArrayList<DatastreamProfile> getFedoraObjectsImageDatastream(String terms) throws FedoraClientException {
		List<String> locatedObjects = findFedoraObjectsUsingSearchTerms(terms);
		ArrayList<DatastreamProfile> result = new ArrayList<DatastreamProfile>();

		for (String pid : locatedObjects) {
			GetDatastreamResponse getDatastreamResponse = null;
			try{
			getDatastreamResponse = FedoraClient.getDatastream(pid, "img").execute();
			}
			catch(FedoraClientException ex){
				if (ex.getMessage().contains("with datastream ID of \"img\"")){
					continue;
				}
				else{
					throw ex;
				}
			}
			result.add(getDatastreamResponse.getDatastreamProfile());
		}

		return result;
	}
	private ArrayList<DatastreamProfile> getFedoraObjectsImageDatastreamWithQuery(String query) throws FedoraClientException {
		List<String> locatedObjects = findFedoraObjectsQuery(query);
		ArrayList<DatastreamProfile> result = new ArrayList<DatastreamProfile>();

		for (String pid : locatedObjects) {
			GetDatastreamResponse getDatastreamResponse = FedoraClient.getDatastream(pid, "img").execute();
			result.add(getDatastreamResponse.getDatastreamProfile());
		}

		return result;
	}

}
