package search;

import java.util.Set;

import org.apache.solr.client.solrj.SolrServerException;

import common.Service;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

/**
 * The {@link Search} class is a {@link Service} responsible for conducting the
 * search for the digital objects.
 * 
 * @author mthnox003
 *
 */
public class Search extends Service {

	/**
	 * The {@link String} value indicating what feature is doing the searching
	 */
	private static final String FEATURE = "search";

	/**
	 * This finds fedora digital objects matching the search terms specified
	 * 
	 * @param terms
	 *            {@link String} instance representing the search terms and
	 *            keywords
	 * @return {@link Set} instance representing the feodra digital objects
	 *         matching the specified search term
	 * @throws FedoraException
	 * @throws SolrServerException
	 */
	public Set<FedoraDigitalObject> findFedoraDigitalObjects(String terms)
			throws FedoraException, SolrServerException {
		return getFedoraCommunicator().findFedoraDigitalObjects(terms, FEATURE);
	}

}
