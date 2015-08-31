package search;

import java.util.List;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;
import common.Service;

public class Search extends Service {

	public Search() {
		super();
	}
	
	public List<DatastreamProfile> findObjects(String terms) throws FedoraClientException{
		return getFedoraCommunicator().findFedoraObjects(terms);
	}
	
	public List<DatastreamProfile> findObjectsWithQuery(String query) throws FedoraClientException{
		return getFedoraCommunicator().findFedoraObjectsWithQuery(query);
	}
}
