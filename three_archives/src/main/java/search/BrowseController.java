package search;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import history.History;
import history.HistoryController;

public class BrowseController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Browse.initialise();
		this.browseFedoraObjects(request, response);
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		(new HistoryController()).execute(request, response);
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
			throws Exception {
		String category = request.getParameter("category");
		String value = request.getParameter(category);
		/*
		 * if the category is blank or null it means that it is just general
		 * browse
		 */
		if (category == null || category.isEmpty()) {
			browseAllFedoraObjects(request, response);
		} else {
			/*
			 * we want to add these values to our word cloud...both the category and the actual value
			 * 
			 */
			History.addTextToTagCloud(category);
			History.addTextToTagCloud(value);
			/*
			 * our category is not null...therefore we need to start filtering
			 * the searches by what has been selected by the user
			 */
			Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjects(), category, value);//we are filtering through all the digital objects...look at filtering filtered objects when thinking about being able to filter the search
			request.setAttribute("browseCategory", category);
			request.setAttribute("categoryValue", value);
			request.getSession().setAttribute("objects", Browse.getFilteredDigitalObjects());
			//this is where we should call the HistoryBrowseMethod to update what the user browses by the most
//			(new HistoryController()).execute(request, response);
		}
//		request.setAttribute("browseCategories", Browse.getFilteredBrowsingCategories()); this will only be relevant when we can do filtering on filtering
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
	}

}
