package search;

import java.util.ArrayList;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import common.fedora.FedoraClient;
import common.fedora.FedoraGetRequest;

/**
 * The {@code SolrCommunicator} is responsible for communication with the Solr
 * Lucene index search engine by using the SolrQuery API provided by apache.
 * This class contains methods to make queries to the solr search engine as well
 * as to allow for the re-indexing via fedoraGsearch and optimising of the index
 * after new digital objects have been introduced into the fedora digital
 * repository
 * 
 * @author mthnox003
 *
 */
public class SolrCommunicator {

	/**
	 * The {@link String} instance representing the URL of the object collection
	 * within the server and within solr Here we see that we are accessing
	 * "collection1"
	 */
	private static final String URL = "http://localhost:8089/solr/collection1";
	/**
	 * The {@link SolrServer} instance allowing for the connection to the Solr
	 * server URL specified in {@link #URL}
	 */
	private static final SolrServer SOLR;

	/**
	 * Static initialiser initialising our single instance of the SolrServer to
	 * be used to communicate with the search engine
	 */
	static {
		SOLR = new HttpSolrServer(URL);
	}

	/**
	 * This method is responsible for building up the SolrQuery to be sent to
	 * the server. This method ensures that the correct element of the API is
	 * being called by specificying the query as "q" and by indicating that the
	 * only field to be returned is the PID
	 * 
	 * @param query
	 *            the {@link String} instance representing the query and search
	 *            keywords and terms to be sent through to the Solr search
	 *            engine
	 * @return {@link SolrQuery} instance of the built up query to be
	 *         propagagted to the search engine
	 */
	private static SolrQuery solrQuery(String query) {

		System.out.println("in SOLR Query");
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.set("q", query);
		solrQuery.setStart(0);
		solrQuery.setRows(1000000000);
		solrQuery.setFields("PID");

		return solrQuery;
	}

	/**
	 * This method is responsible for sending the query through to the search
	 * engine and retrieving a response once the query has been executed.
	 * 
	 * @param solrQuery
	 *            {@link SolrQuery} instance containing the query to be sent to
	 *            the Solr search engine
	 * @return {@link ArrayList} instance of PIDs matching the keywords within
	 *         the query
	 * @throws SolrServerException
	 */
	private static ArrayList<String> solrResponse(SolrQuery solrQuery)
			throws SolrServerException {
		System.out.println("in SOLR response");
		QueryResponse queryResponse = SOLR.query(solrQuery);
		SolrDocumentList list = queryResponse.getResults();
		System.out.println("SOLR response size: " + list.size());
		System.out.println("SOLR response size: " + list.getNumFound());
		ArrayList<String> pids = new ArrayList<String>();
		for (int x = 0; x < list.getNumFound(); x++) {
			pids.add((String) list.get(x).get("PID"));
		}
		System.out.println("Result found : " + pids.toString());
		return pids;
	}

	/**
	 * This is our entry point into the Solr Search engine and to the
	 * SolrCommunicator class when making queries. This is where the query will
	 * be constructed and executed in order to obtain a response
	 */
	public static ArrayList<String> solrSearch(String query)
			throws SolrServerException {
		return solrResponse(solrQuery(query));
	}

	/**
	 * This method allows for the updating of the SOLR index using the
	 * FedoraGsearch service once any values or updates to the digital objects
	 * contained within the fedora digital object repository have taken place
	 * 
	 * @throws Exception
	 */
	public static void updateSolrIndex() throws Exception {
		System.out.println("about to update solr index");
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();
		StringBuilder query = new StringBuilder(
				"http://localhost:8089/fedoragsearch/rest?operation=updateIndex&action=fromFoxmlFiles&value=");
		fedoraGetRequest.setRequest(query);
		System.out.println("UPDATING SOLR INDEX");
		FedoraClient.executeWithoutParsingResponse(fedoraGetRequest);
		System.out.println("Successfully updated index with new objects");
		optimizeSolrIndex();

	}

	/**
	 * This method is resonsible for the optimisation of the index after updates
	 * and re-indexing of the digital objects has occurred
	 * 
	 * @throws Exception
	 */
	private static void optimizeSolrIndex() throws Exception {
		System.out.println("about to optimize solr index");
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();
		StringBuilder query = new StringBuilder(
				"http://localhost:8089/fedoragsearch/rest?operation=updateIndex&action=optimize");
		fedoraGetRequest.setRequest(query);
		System.out.println("OPTIMIZING INDEX");
		FedoraClient.executeWithoutParsingResponse(fedoraGetRequest);
		System.out.println("Successfully optimised index");
	}
}
