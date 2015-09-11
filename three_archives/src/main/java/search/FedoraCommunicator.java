package search;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import common.fedora.Datastream;
import common.fedora.DatastreamID;
import common.fedora.DublinCore;
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
	
	
	public ArrayList<FedoraDigitalObject> findFedoraDigitalObjects(String terms) throws FedoraException{
		return findAndPopulateFedoraDigitalObjects(terms);
	}
	
	
	public ArrayList<Datastream> findFedoraDatastreams(String terms)
			throws FedoraException {
		return findFedoraDatastreamWithSpecificDatastreamIDMatchingTerms(terms, DatastreamID.DC);
	}
	
	
	private ArrayList<FedoraDigitalObject> findAndPopulateFedoraDigitalObjects(String terms) throws FedoraException{
		ArrayList<FedoraDigitalObject> results = new ArrayList<FedoraDigitalObject>();
		List<String> pids = findFedoraObjectsWithSearchTerm(terms);
		try {
			for (String pid : pids) {
				System.out.println("Processing digital object with pid " + pid);
				results.add(populateFedoraDigitalObject(pid));
			}
		} catch (Exception ex) {
			throw new FedoraException("Could not Populate fedora digital objects",ex);
		}

		return results;
	}
	
	private ArrayList<Datastream> findFedoraDatastreamWithSpecificDatastreamIDMatchingTerms(String terms, DatastreamID datastreamID)
			throws FedoraException {
		ArrayList<Datastream> results = new ArrayList<Datastream>();
		List<String> pids = findFedoraObjectsWithSearchTerm(terms);
		try {
			for (String pid : pids) {
				System.out.println("Processing digital object with pid " + pid);
				results.add(findSpecificDatastreamsForFedoraObject(pid,
						datastreamID));
			}
		} catch (Exception ex) {
			throw new FedoraException("Could not find data streams with specific type",ex);
		}

		return results;
	}

	private List<String> findFedoraObjectsWithSearchTerm(String terms)
			throws FedoraException {
		TreeMap<QueryParameters, String> queryParameters = new TreeMap<QueryParameters, String>();
		queryParameters.put(QueryParameters.TERMS, terms);
		queryParameters.put(QueryParameters.RESULT_FORMAT, "xml");

		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();

		List<String> pids;
		try {
			pids = fedoraClient.execute(
					fedoraGetRequest.findObjects(queryParameters,
							DublinCore.PID)).parseFindObjects();
			;
			if (pids == null || pids.isEmpty()) {
				throw new FedoraException("Could not find any results for your search");
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
			DatastreamID datastreamId) throws FedoraException,
			ParserConfigurationException, SAXException, IOException {
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		Datastream datastream;
		datastream = fedoraClient.execute(
				fedoraGetRequest.getDatastream(datastreamId.name(), null))
				.parseGetDatastream();
		DatastreamID dsid = datastream.getDatastreamIdentifier();

		if (dsid.equals(DatastreamID.DC.name())) {
			datastream = fedoraClient.execute(
					fedoraGetRequest.getDatastreamDissemination(dsid.name(),
							null)).parseDublinCoreDatastream(datastream);
		}

		return datastream;
	}

	private FedoraDigitalObject populateFedoraDigitalObject(String pid)
			throws FedoraException {
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(pid);
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		fedoraClient.execute(fedoraGetRequest.getObjectProfile(null))
				.parseGetObjectProfile(fedoraDigitalObject);
		InputStream xml = fedoraClient.execute(fedoraGetRequest.getObjectXML())
				.getResponse();
		List<String> versionHistory = fedoraClient.execute(
				fedoraGetRequest.getObjectHistory()).parseGetObjectHistory();
		fedoraDigitalObject.setXmlRepresentation(xml);
		fedoraDigitalObject.setVersionHistory(versionHistory);

		// we now need to list and then parse all of the datastreams for this
		// digital object
		// list the ds and then call findSpecificDatastreamsForFedoraObject
		List<DatastreamID> datastreamIds = fedoraClient.execute(
				fedoraGetRequest.listDatastreams(null)).parseListDataStream();
		List<Datastream> objectDatastreams = new ArrayList<Datastream>();
		try {
			for (DatastreamID datastreamID : datastreamIds) {
				objectDatastreams.add(findSpecificDatastreamsForFedoraObject(pid, datastreamID));
			}
		} catch (Exception ex) {
			throw new FedoraException("Could not populate fedora digital object",ex);
		}
		fedoraDigitalObject.setDatastreams(objectDatastreams);
		return fedoraDigitalObject;
	}

}
