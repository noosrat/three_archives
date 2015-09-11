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
		this.client = ApacheHttpClient.create();
		client.addFilter(new HTTPBasicAuthFilter(FedoraCredentials
				.getUsername(), FedoraCredentials.getPassword()));
	}
	
	public FedoraXMLResponseParser execute(FedoraGetRequest fedoraGetRequest) throws FedoraException{
		WebResource webResource = client.resource(fedoraGetRequest.getRequest().toString());
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		fedoraGetRequest.resetRequest();
		
		if (clientResponse.getStatus()==200){
			return new FedoraXMLResponseParser(clientResponse.getEntityInputStream());
		}
		else{
			throw new FedoraException("Request execution unsuccessful " + clientResponse.getStatus());
		}

	}
}
