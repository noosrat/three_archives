package search;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import common.controller.Controller;
import common.fedora.DatastreamID;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class BrowseController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Browse.initialise();
		this.browseFedoraObjects(request, response);
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		System.out.println("SEARCH CATEGORIES " + SearchController.retrieveSearchCategories());
		return "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
	}

	private void browseAllFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException, SolrServerException {

		// this is to return everything in the archive collection...this is just
		// to illustrate browse temporarily
		/* we are intiially searching all the fedora objects here */
		Set<FedoraDigitalObject> fedoraDigitalObjects = Browse.getFedoraDigitalObjects();

		if (fedoraDigitalObjects != null && !fedoraDigitalObjects.isEmpty()) {
			request.getSession().setAttribute("objects", fedoraDigitalObjects);
		} else {
			request.setAttribute("message", "No results to return");
		}
	}

	private void browseFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException, SolrServerException {
		String category = request.getParameter("category");
		String value = request.getParameter(category);

		System.out.println("Browse by category " + category + " value " + value);

		/*
		 * if the category is blank or null it means that it is just general
		 * browse
		 */
		if (category == null || category.isEmpty()) {
			browseAllFedoraObjects(request, response);
		} else {

			/*
			 * our category is not null...therefore we need to start filtering
			 * the searches by what has been selected by the user
			 */
			Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjects(), category, value);//we are filtering through all the digital objects...look at filtering filtered objects when thinking about being able to filter the search
			request.setAttribute("browseCategory", category);
			request.setAttribute("categoryValue", value);
			request.getSession().setAttribute("objects", Browse.getFilteredDigitalObjects());

		}
		System.out.println("Categories " + Browse.getFilteredBrowsingCategories());
//		request.setAttribute("browseCategories", Browse.getFilteredBrowsingCategories()); this will only be relevant when we can do filtering on filtering
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
	}

	private Set<String> matchingSearchTags(FedoraDigitalObject fedoraDigitalObject){ //this is taken from the metadata of the fedora object and links are given..when the links are clicked a search is conducted on that value
		Set<String> results = new TreeSet<String>();
		//here we compile a list of results from the metadata items and we turn them into tags? or rather make the metadata tags in the view clickable links?
		DublinCoreDatastream dc = (DublinCoreDatastream)fedoraDigitalObject.getDatastreams().get(DatastreamID.DC.name());
		
		for (String s: dc.getDublinCoreMetadata().values()){
			String[] split = s.split(";");
			for (String s2:split){
				String[] splitmore = s2.split("%");
				results.addAll(Arrays.asList(splitmore));
			}
		}
		
		return results;
	}

}
