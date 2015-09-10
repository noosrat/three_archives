package search;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import common.fedora.DublinCore;
import common.fedora.FedoraClient;
import common.fedora.FedoraException;
import common.fedora.FedoraGetRequest;
import common.fedora.QueryParameters;

public class FedoraCommunicator {
	/*
	 * 1. FindObjects (according to search terms) = list of pids 2. Create
	 * Fedora Digital objects with these pids 3. FindObjectXML 4.
	 * FindObjectHistory 5. ListDatastreams = data stream ids 6. GetActual
	 * datastreams and populate
	 */

	private FedoraClient fedoraClient;

	public FedoraCommunicator() {
		fedoraClient = new FedoraClient();
	}

	
	public static void main(String[] args) throws FedoraException{
		new FedoraCommunicator().findFedoraObjectsWithSearchTerm(null);
	}
	
	
	private void findFedoraObjectsWithSearchTerm(String terms) throws FedoraException{
		TreeMap<QueryParameters, String> queryParameters = new TreeMap<QueryParameters, String>();
		queryParameters.put(QueryParameters.TERMS, "Spring+Queen");
		queryParameters.put(QueryParameters.RESULT_FORMAT, "xml");
		ArrayList<DublinCore> toReturn = new ArrayList<DublinCore>();
		toReturn.add(DublinCore.PID);
		
		FedoraGetRequest fedoraGetRequest = new FedoraGetRequest();
		fedoraClient.execute(fedoraGetRequest.findObjects(queryParameters, toReturn));

		List<String> pids;
		try{
		pids = fedoraGetRequest.getFedoraResponse().parseFindObjects();
		if (pids==null || pids.isEmpty()){
			throw new FedoraException("Could not find any results for your search");
		}
		}
		catch(Exception ex){
			throw new FedoraException("Could not parse xml response " + ex.getMessage() + ex.getCause());
		}
		
		
		for (String id: pids){
		
			System.out.println(id);
		}
	}
}
