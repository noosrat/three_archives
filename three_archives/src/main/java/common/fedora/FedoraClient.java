package common.fedora;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class FedoraClient {
	private static Client client;

	static {
		client = ApacheHttpClient.create();
		client.addFilter(new HTTPBasicAuthFilter(FedoraCredentials.getUsername(), FedoraCredentials.getPassword()));
	}
	private FedoraClient(){
		
	}

	public static Client getClient() {
		return client;
	}
	
	public static void executeWithoutParsingResponse(FedoraGetRequest fedoraGetRequest) throws FedoraException{
		WebResource webResource = client.resource(fedoraGetRequest.getRequest().toString());
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		clientResponse.getEntityInputStream();
	}

	public static FedoraXMLResponseParser execute(FedoraGetRequest fedoraGetRequest) throws FedoraException {
		WebResource webResource = client.resource(fedoraGetRequest.getRequest().toString());
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		fedoraGetRequest.resetRequest();

		if (clientResponse.getStatus() == 200) {
			return new FedoraXMLResponseParser(clientResponse.getEntityInputStream());
		} else {
			throw new FedoraException("Request execution unsuccessful " + clientResponse.getStatus());
		}

	}
}