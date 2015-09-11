package search;

import java.util.List;

import common.Service;
import common.fedora.Datastream;
import common.fedora.FedoraException;

public class Search extends Service {

	public Search() {
		super();
	}
	
	public List<Datastream> findObjects(String terms) throws FedoraException{
		return getFedoraCommunicator().findFedoraObjects(terms);
	}
//	
//	public List<DatastreamProfile> findObjectsWithQuery(String query) throws FedoraClientException{
//		return getFedoraCommunicator().findFedoraObjectsWithQuery(query);
//	}
}
