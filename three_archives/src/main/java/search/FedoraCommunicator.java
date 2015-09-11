package search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import common.fedora.Datastream;
import common.fedora.DatastreamId;
import common.fedora.DublinCore;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraClient;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import common.fedora.FedoraGetRequest;
import common.fedora.QueryParameters;

public class FedoraCommunicator {
	/*
	 * 1. FindObjects (according to search terms) = list of pids 2. Create
	 * Fedora Digital objects with these pids and getObjectProfile 3.
	 * FindObjectXML (easy to obtain) 4. FindObjectHistory (not necessary) 5.
	 * ListDatastreams = data stream ids 6. GetActual datastreams and populate
	 */

	private FedoraClient fedoraClient;

	public FedoraCommunicator() {
		fedoraClient = new FedoraClient();
	}

	public static void main(String[] args) throws FedoraException {
		new FedoraCommunicator().findFedoraObjectsWithSearchTerm(null);
	}

	public ArrayList<Datastream> findFedoraObjects(String terms)
			throws FedoraException {
		return findFedoraObjectss("Spring+Queen");
	}

	private ArrayList<Datastream> findFedoraObjectss(String terms)
			throws FedoraException {
		ArrayList<Datastream> results = new ArrayList<Datastream>();
		List<String> pids = findFedoraObjectsWithSearchTerm("");
		try {
			for (String pid : pids) {
				System.out.println("Processing digital object with pid " + pid);
				results.add(findSpecificDatastreamsForFedoraObject(pid,
						DatastreamId.img));
			}
		} catch (Exception ex) {
			throw new FedoraException(ex);
		}

		return results;
	}

	private List<String> findFedoraObjectsWithSearchTerm(String terms)
			throws FedoraException {
		TreeMap<QueryParameters, String> queryParameters = new TreeMap<QueryParameters, String>();
		queryParameters.put(QueryParameters.TERMS, "Spring+Queen");
		queryParameters.put(QueryParameters.RESULT_FORMAT, "xml");

		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();

		List<String> pids;
		try {
			pids = fedoraClient.execute(
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

	/* this will give you one specific datastream for a specified object */
	private Datastream findSpecificDatastreamsForFedoraObject(String pid,
			DatastreamId datastreamId) throws FedoraException,
			ParserConfigurationException, SAXException, IOException {
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		TreeMap<QueryParameters, String> queryParameters = new TreeMap<QueryParameters, String>();
		Datastream datastream;
		datastream = fedoraClient.execute(
				fedoraGetRequest.getDatastream(datastreamId.name(), null))
				.parseGetDatastream();
		DatastreamId dsid = datastream.getDatastreamIdentifier();
		if (dsid.equals(DatastreamId.DC)) {
			fedoraGetRequest = new FedoraGetRequest(pid);
			fedoraClient.execute(
					fedoraGetRequest.getDatastreamDissemination(dsid.name(),
							null)).parseDublinCoreDatastream(
					(DublinCoreDatastream) (datastream));
		}

		return datastream;
	}

	private FedoraDigitalObject populateFedoraDigitalObject(String pid)
			throws FedoraException {
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(pid);
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		fedoraClient.execute(fedoraGetRequest.getObjectProfile(null))
				.parseGetObjectProfile(fedoraDigitalObject);

		return fedoraDigitalObject;
	}

}
