package common.fedora;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;

/**
 * The {@code FedoraClient} is responsible for directly interfacing with the
 * Fedora RESTful API which can be found at {@link } The FedoraClient is
 * responsible for the execution of the Fedora request
 * 
 * 
 * @author mthnox003
 *
 */
public class FedoraClient {
	/**
	 * The {@link Client} instance of the fedora Client. This is used to
	 * communicate with the restful API
	 */
	private static final Client CLIENT;

	/**
	 * A static initialiser for the {@link FedoraClient} intialising the Client
	 * to be used throughout the applications interaction with the Fedora
	 * digital object repository service The intiialiser creates the
	 * {@link ApacheHTtpClient} and assigns the credentials as per
	 * {@link FedoraCredentials}
	 */
	static {
		CLIENT = ApacheHttpClient.create();
		CLIENT.addFilter(new HTTPBasicAuthFilter(FedoraCredentials.getUsername(), FedoraCredentials.getPassword()));
	}

	/**
	 * Default constructor for the FedoraClient
	 */
	private FedoraClient() {
	}

	/**
	 * Gets the client of the FedoraClient
	 * 
	 * @return {@link Client} instance of the {@link FedoraClient}
	 */
	public static Client getClient() {
		return CLIENT;
	}

	/**
	 * This allows for the execution of the Fedora Request without parsing the
	 * response. This is relevant in situations where the response is not
	 * required i.e. during the re-indexing process of the Fedora Digital
	 * objects as in {@link SolrCommunicator.updateSolrIndex() and
	 * SolrCommunicator.optimizeSolrIndex()
	 * 
	 * @param fedoraGetRequest
	 *            {@link FedoraGetRequest} instance containing the built up
	 *            request to be forwarded to the fedora REST API
	 * 
	 */
	public static void executeWithoutParsingResponse(FedoraGetRequest fedoraGetRequest) {
		WebResource webResource = CLIENT.resource(fedoraGetRequest.getRequest().toString());
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		clientResponse.getEntityInputStream();
	}

	/**
	 * This allos for the execution of the Fedora Get request with parsing the
	 * response retrieved
	 * 
	 * @param fedoraGetRequest
	 *            {@link FedoraGetRequest} instance containing the built up
	 *            request to be forwarded to the Fedora REST API
	 * @return {@link FedoraXMLResponseParser} instance where the response
	 *         retrieved will be parsed into a local {@link FedoraDigitalObject}
	 * @throws FedoraException
	 *             when the response is not a successful response. When the
	 *             response code is anything besides 200
	 */
	public static FedoraXMLResponseParser execute(FedoraGetRequest fedoraGetRequest) throws FedoraException {
		WebResource webResource = CLIENT.resource(fedoraGetRequest.getRequest().toString());
		ClientResponse clientResponse = webResource.get(ClientResponse.class);
		fedoraGetRequest.resetRequest();

		if (clientResponse.getStatus() == 200) {
			return new FedoraXMLResponseParser(clientResponse.getEntityInputStream());
		} else {
			throw new FedoraException("Request execution unsuccessful " + clientResponse.getStatus());
		}

	}
}