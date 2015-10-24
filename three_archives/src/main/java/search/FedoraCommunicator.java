package search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.solr.client.solrj.SolrServerException;
import org.xml.sax.SAXException;

import common.fedora.Datastream;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
import common.fedora.FedoraClient;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import common.fedora.FedoraGetRequest;
import common.fedora.QueryParameter;

/**
 * The {@link FedoraCommunicator} allows for communication with the fedora
 * digital object repository and allows for querying of objects by either
 * querying the repostitory or by querying the solr search engine
 * 
 * /* 1. FindObjects (according to search terms) = list of pids 2. Create Fedora
 * Digital objects with these pids and getObjectProfile 3. FindObjectXML 4.
 * FindObjectHistory 5. ListDatastreams = data stream ids 6. GetActual
 * datastreams and populate
 *
 * @author mthnox003
 *
 */
public class FedoraCommunicator {

	/**
	 * This finds fedora digital objects based on the search term entered using
	 * the interface
	 * 
	 * @param terms
	 *            {@link String} instance of search terms
	 * @param feature
	 *            {@link String} instance representing which feature has
	 *            resulted in the search (browse or maps)
	 * @return {@link Set} instance representing the fedora digital objects
	 *         satisfying the search terms
	 * @throws FedoraException
	 * @throws SolrServerException
	 */
	public Set<FedoraDigitalObject> findFedoraDigitalObjects(String terms,
			String feature) throws FedoraException, SolrServerException {
		return findAndPopulateFedoraDigitalObjects(terms, feature);
	}

	/**
	 * This method, dependent on the feature which has resulted in the search
	 * either consults the Solr search engine or the fedora digital repository
	 * for the objects matching the terms The search results are in the form of
	 * a list of PIDs which are then further processed in order to obtain the
	 * fully populated FedoraDigitalObjects
	 * 
	 * @param terms
	 *            {@link String} instance of search terms
	 * @param feature
	 *            {@link String} instance representing which feature has
	 *            resulted in the search (browse or maps)
	 * @return {@link Set} instance representing the fedora digital objects
	 *         satisfying the search terms @throws FedoraException
	 * @throws SolrServerException
	 */
	private Set<FedoraDigitalObject> findAndPopulateFedoraDigitalObjects(
			String terms, String feature) throws FedoraException,
			SolrServerException {
		System.out.println("Finding fedora digital objects matching: " + terms);
		Set<FedoraDigitalObject> results = new HashSet<FedoraDigitalObject>();
		List<String> pids = new ArrayList<String>();
		if (feature == "search") {
			pids = SolrCommunicator.solrSearch(terms);

		} else if (feature == "maps") {
			pids = findFedoraObjectsWithSearchTerm(terms);
		}
		try {
			for (String pid : pids) {
				System.out.println("Processing digital object with pid " + pid);
				results.add(populateFedoraDigitalObject(pid));
			}
		} catch (Exception ex) {
			System.out.print(ex);
			throw new FedoraException(
					"Could not Populate fedora digital objects", ex);
		}
		return results;
	}

	/**
	 * This allows for querying of the fedora repository to obtain results
	 * matching the search terms
	 * 
	 * @param terms
	 *            {@link String} instance of search terms
	 * @return {@link List} instance containing a list of PIDs matching hte
	 *         specified search terms
	 * @throws FedoraException
	 */
	private List<String> findFedoraObjectsWithSearchTerm(String terms)
			throws FedoraException {
		TreeMap<QueryParameter, String> queryParameters = new TreeMap<QueryParameter, String>();
		queryParameters.put(QueryParameter.TERMS, terms);
		queryParameters.put(QueryParameter.RESULT_FORMAT, "xml");

		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();

		List<String> pids;
		try {
			pids = FedoraClient.execute(
					fedoraGetRequest.findObjects(queryParameters,
							DublinCore.PID)).parseFindObjects();
			;
			if (pids == null || pids.isEmpty()) {
				throw new FedoraException(
						"Could not find any results for your search");
			}
		} catch (Exception ex) {
			throw new FedoraException("Could not parse xml response "
					+ ex.getMessage() + ex.getCause());
		}
		System.out.println("Successfully located " + pids.size()
				+ " digital objects");
		return pids;
	}

	/**
	 * This will give you one specific datastream for a specified object
	 * 
	 * @param pid
	 *            {@link String} instance representing the PID of the object
	 *            whose datastream is being queried
	 * @param datastreamId
	 *            {@link DatastreamID} instance representing which datastream is
	 *            being requested
	 * @return {@link Datastream} instance of the datastream with the specified
	 *         pid and datastreamId
	 * @throws FedoraException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Datastream findSpecificDatastreamsForFedoraObject(String pid,
			DatastreamID datastreamId) throws FedoraException,
			ParserConfigurationException, SAXException, IOException {
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		Datastream datastream;
		datastream = FedoraClient.execute(
				fedoraGetRequest.getDatastream(datastreamId.name(), null))
				.parseGetDatastream();
		DatastreamID dsid = datastream.getDatastreamID();

		if (dsid.equals(DatastreamID.DC)) {
			System.out
					.println("Located a dublin core datastream.  About to parse this type");
			datastream = FedoraClient.execute(
					fedoraGetRequest.getDatastreamDissemination(dsid.name(),
							null)).parseDublinCoreDatastream(datastream);
		}

		return datastream;
	}

	/**
	 * This method is responsible for populating the fedora digital object
	 * 
	 *
	 * 1. Create Fedora Digital objects with the pid 2. getObjectProfile 3.
	 * FindObjectXML 4. FindObjectHistory 5. ListDatastreams = data stream ids
	 * 6. GetActual datastreams and populate
	 * 
	 * 
	 * 
	 * @param pid
	 *            {@link String} instance representing the PID of the object
	 *            being populated
	 * @return {@link FedoraDigitalObject} with all the relevant fields
	 *         populated
	 * @throws FedoraException
	 */
	public FedoraDigitalObject populateFedoraDigitalObject(String pid)
			throws FedoraException {
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(pid);
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		FedoraClient.execute(fedoraGetRequest.getObjectProfile(null))
				.parseGetObjectProfile(fedoraDigitalObject);

		InputStream xml = FedoraClient.execute(fedoraGetRequest.getObjectXML())
				.getResponse();
		List<String> versionHistory = FedoraClient.execute(
				fedoraGetRequest.getObjectHistory()).parseGetObjectHistory();
		fedoraDigitalObject.setXmlRepresentation(xml);
		fedoraDigitalObject.setVersionHistory(versionHistory);

		// we now need to list and then parse all of the datastreams for this
		// digital object
		// list the ds and then call findSpecificDatastreamsForFedoraObject

		List<DatastreamID> datastreamIds = FedoraClient.execute(
				fedoraGetRequest.listDatastreams(null)).parseListDataStream();

		HashMap<String, Datastream> objectDatastreams = new HashMap<String, Datastream>();

		try {
			for (DatastreamID datastreamID : datastreamIds) {
				objectDatastreams.put(
						datastreamID.name(),
						findSpecificDatastreamsForFedoraObject(pid,
								datastreamID));
			}
		} catch (Exception ex) {
			System.out.println(ex);
			throw new FedoraException(
					"Could not populate fedora digital object", ex);
		}
		fedoraDigitalObject.setDatastreams(objectDatastreams);
		return fedoraDigitalObject;
	}

	/**
	 * @author noosrat
	 * @param obs
	 * @throws FedoraException
	 */
	public void downloadFedoraDigitalObjectUsingObjects(
			Set<FedoraDigitalObject> obs) throws FedoraException {
		for (FedoraDigitalObject object : obs) {
			System.out.println(object.getPid());

			// pid =object.getPid();

			FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(
					object.getPid());
			TreeMap<QueryParameter, String> param = new TreeMap();
			// TreeMap param = new TreeMap();

			// QueryParameters qp = new QueryParameters("download");

			param.put(QueryParameter.DOWNLOAD, "true");
			FedoraClient.execute((fedoraGetRequest.getDatastreamDissemination(
					"IMG", param)).getObjectXML());

		}
	}

}