package maps;

import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.Datastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class Map extends Service {

	public Map() {
		super();
	}
	
	public List<Datastream> findFedoraDatastreams(String terms) throws FedoraException{
		return getFedoraCommunicator().findFedoraDatastreamsUsingSearchTerms(terms);
	}
	
	public List<FedoraDigitalObject> findFedoraDigitalObjects(String terms) throws FedoraException, SolrServerException{
//		return getFedoraCommunicator().findFedoraDigitalObjectsUsingSearchTerms(terms);
		return getFedoraCommunicator().findFedoraDigitalObjects(terms);
	}
}