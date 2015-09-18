package search;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import common.controller.Controller;
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
			request.setAttribute("objects", fedoraDigitalObjects);
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
			Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjects(), category, value);
			request.setAttribute("objects", Browse.getFilteredDigitalObjects());

		}
		System.out.println("Categories " + Browse.getFilteredBrowsingCategories());
		request.setAttribute("browseCategories", Browse.getFilteredBrowsingCategories());
	}

}
