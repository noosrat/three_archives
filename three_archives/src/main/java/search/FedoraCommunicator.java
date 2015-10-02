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
import common.fedora.QueryParameters;

public class FedoraCommunicator {
	/*
	 * 1. FindObjects (according to search terms) = list of pids 2. Create
	 * Fedora Digital objects with these pids and getObjectProfile 3.
	 * FindObjectXML (easy to obtain) 4. FindObjectHistory (not necessary) 5.
	 * ListDatastreams = data stream ids 6. GetActual datastreams and populate
	 */


	public Set<FedoraDigitalObject> findFedoraDigitalObjects(String terms, String feature) throws FedoraException, SolrServerException{
		return findAndPopulateFedoraDigitalObjects(terms, feature);
	}
	
	
	public HashMap<String,Datastream> findFedoraDatastreamsUsingSearchTerms(String terms)
			throws FedoraException {
		return findFedoraDatastreamWithSpecificDatastreamIDMatchingTerms(terms, DatastreamID.DC);
	}
	
	

	private Set<FedoraDigitalObject> findAndPopulateFedoraDigitalObjects(String terms, String feature) throws FedoraException, SolrServerException{

		System.out.println("Finding fedora digital objects matching: " + terms);

		Set<FedoraDigitalObject> results = new HashSet<FedoraDigitalObject>();
		
		List<String> pids = new ArrayList<String>();
		if (feature=="search"){

			pids = SolrCommunicator.solrSearch(terms);

		} else if (feature=="maps") {
			pids = findFedoraObjectsWithSearchTerm(terms);
		}	
		
		try {
			for (String pid : pids) {
				System.out.println("Processing digital object with pid " + pid);
				results.add(populateFedoraDigitalObject(pid));
			}
		} catch (Exception ex) {
			System.out.print(ex);
			throw new FedoraException("Could not Populate fedora digital objects",ex);
		}

		return results;
	}

	
	private HashMap<String,Datastream> findFedoraDatastreamWithSpecificDatastreamIDMatchingTerms(String terms, DatastreamID datastreamID)
			throws FedoraException {
		HashMap<String,Datastream> results = new HashMap<String,Datastream>();
		List<String> pids = findFedoraObjectsWithSearchTerm(terms);
		try {
			for (String pid : pids) {
				System.out.println("Processing digital object with pid " + pid);
				results.put(datastreamID.name(),findSpecificDatastreamsForFedoraObject(pid,
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
			pids = FedoraClient.execute(fedoraGetRequest.findObjects(queryParameters,DublinCore.PID)).parseFindObjects();
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
		datastream = FedoraClient.execute(
				fedoraGetRequest.getDatastream(datastreamId.name(), null))
				.parseGetDatastream();
		DatastreamID dsid = datastream.getDatastreamID();

		if (dsid.equals(DatastreamID.DC)) {
			System.out.println("Located a dublin core datastream.  About to parse this type");
			datastream = FedoraClient.execute(
					fedoraGetRequest.getDatastreamDissemination(dsid.name(),
							null)).parseDublinCoreDatastream(datastream);
		}

		return datastream;
	}

	public FedoraDigitalObject populateFedoraDigitalObject(String pid)
			throws FedoraException {
		System.out.println(" populating xxxxxxxxxxxxxxxxxxxxxx");
		FedoraDigitalObject fedoraDigitalObject = new FedoraDigitalObject(pid);
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(pid);
		FedoraClient.execute(fedoraGetRequest.getObjectProfile(null))
				.parseGetObjectProfile(fedoraDigitalObject);

		InputStream xml = FedoraClient.execute(fedoraGetRequest.getObjectXML())
				.getResponse();
		System.out.println(" happened");
		List<String> versionHistory = FedoraClient.execute(
				fedoraGetRequest.getObjectHistory()).parseGetObjectHistory();
		fedoraDigitalObject.setXmlRepresentation(xml);
		fedoraDigitalObject.setVersionHistory(versionHistory);
		System.out.println(" happened");

		// we now need to list and then parse all of the datastreams for this
		// digital object
		// list the ds and then call findSpecificDatastreamsForFedoraObject

		List<DatastreamID> datastreamIds = FedoraClient.execute(
				fedoraGetRequest.listDatastreams(null)).parseListDataStream();

		HashMap<String,Datastream> objectDatastreams = new HashMap<String,Datastream>();

		try {
			for (DatastreamID datastreamID : datastreamIds) {
				objectDatastreams.put(datastreamID.name(),findSpecificDatastreamsForFedoraObject(pid, datastreamID));
			}
		} catch (Exception ex) {
			throw new FedoraException("Could not populate fedora digital object",ex);
		}
		fedoraDigitalObject.setDatastreams(objectDatastreams);
		return fedoraDigitalObject;
	}
	
	public void downloadFedoraDigitalObjectUsingObjects(Set<FedoraDigitalObject> obs) throws FedoraException{
		for(FedoraDigitalObject object: obs){
			 System.out.println(object.getPid());
			 
			//pid =object.getPid();
		
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest(object.getPid());
		TreeMap<QueryParameters, String> param = new TreeMap();
		//TreeMap param = new TreeMap();
		
		//QueryParameters qp = new QueryParameters("download");
		
		param.put(QueryParameters.DOWNLOAD, "true");
		FedoraClient.execute((fedoraGetRequest.getDatastreamDissemination("IMG",param)).getObjectXML());
		
		}
	}

}