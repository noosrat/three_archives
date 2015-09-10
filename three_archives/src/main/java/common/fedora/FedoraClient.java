package common.fedora;

import org.apache.log4j.Logger;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class FedoraClient {
	private final static Logger LOG = Logger.getLogger(FedoraClient.class);
	
	private Client client;
	 
	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
	
	
	public FedoraClient(){
		FedoraCredentials fedoraCredentials = new FedoraCredentials();
		this.client = ApacheHttpClient.create();
		client.addFilter(new HTTPBasicAuthFilter(fedoraCredentials
				.getUsername(), fedoraCredentials.getPassword()));
	}
	
	public void execute(FedoraGetRequest fedoraGetRequest) throws FedoraException{
		WebResource webResource = client.resource(fedoraGetRequest.getRequest().toString());
		LOG.debug("Successfully created web resource");
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		
		if (clientResponse.getStatus()==200){
			fedoraGetRequest.setFedoraResponse(new FedoraXMLResponseParser(clientResponse.getEntityInputStream()));
		}
		else{
			throw new FedoraException("Request execution unsuccessful " + clientResponse.getStatus());
		}

	}
}
