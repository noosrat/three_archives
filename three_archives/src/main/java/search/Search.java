package search;

import java.util.HashMap;
import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.Datastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class Search extends Service {

	public Search() {
		super();
	}
	
	
	public HashMap<String,Datastream> findFedoraDatastreams(String terms) throws FedoraException{
		return getFedoraCommunicator().findFedoraDatastreamsUsingSearchTerms(terms);
	}
	
	public Set<FedoraDigitalObject> findFedoraDigitalObjects(String terms) throws FedoraException, SolrServerException{
//		return getFedoraCommunicator().findFedoraDigitalObjectsUsingSearchTerms(terms);
		return getFedoraCommunicator().findFedoraDigitalObjects(terms);
	}
}
