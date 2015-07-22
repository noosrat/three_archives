package services.search;

import java.util.List;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

import services.Service;

public class Search extends Service {

	public Search() {
		super();
	}
	
	public List<DatastreamProfile> findObjects(String terms) throws FedoraClientException{
		return getFedoraCommunicator().findFedoraObjects(terms);
	}
}
