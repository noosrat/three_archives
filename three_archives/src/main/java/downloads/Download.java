package downloads;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class Download extends Service{

	public Download() {
		super();
	}
	
	public Set<FedoraDigitalObject> getFedoraDigitalObjects(ArrayList<String> cart) throws FedoraException
	{
		Set<FedoraDigitalObject> obs = new HashSet<FedoraDigitalObject>();
		for (int k=0; k<cart.size(); k++){
		obs.add(getFedoraCommunicator().populateFedoraDigitalObject(cart.get(k)));
		}
		return obs;
	}
	
	public void downloadFedoraDigitalObjects(Set<FedoraDigitalObject> obs) throws FedoraException, SolrServerException{
//		return getFedoraCommunicator().findFedoraDigitalObjectsUsingSearchTerms(terms);
		getFedoraCommunicator().downloadFedoraDigitalObjectUsingObjects(obs);
	}
}
