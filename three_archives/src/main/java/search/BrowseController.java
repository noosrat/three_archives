package search;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.solr.client.solrj.SolrServerException;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import history.History;
import history.HistoryController;

public class BrowseController implements Controller {

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Browse.initialise((String)request.getSession().getAttribute("mediaPrefix"));
		// String result = browseFedoraObjects(request, response);
		browseFedoraObjects(request, response);
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		(new HistoryController()).execute(request, response);
		// return result;
		return "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
	}

	private void browseAllFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException, SolrServerException {

		// this is to return everything in the archive collection...this is just
		// to illustrate browse temporarily
		/* we are intiially searching all the fedora objects here */
		Set<FedoraDigitalObject> fedoraDigitalObjects = Browse.getFedoraDigitalObjects();
		Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive = Browse.getFedoraDigitalObjectsForArchive(); // we
																												// cannot
																												// do
																												// this
																												// without
																												// first
																												// getting
																												// all
																												// objects
		// before we actually set the attribute we need to filter per archive

		if (fedoraDigitalObjects != null && !fedoraDigitalObjects.isEmpty()) {
			request.getSession().setAttribute("objects", fedoraDigitalObjects);
			if (!(fedoraDigitalObjectsForArchive.isEmpty())) {
				request.getSession().setAttribute("objectsForArchive", fedoraDigitalObjectsForArchive);
			}
		} else {
			request.setAttribute("message", "No results to retuRrn");
		}
	}

	private void browseFedoraObjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
			 * we want to add these values to our word cloud...both the category
			 * and the actual value
			 * 
			 */
			History.addTextToTagCloud(category);
			History.addTextToTagCloud(value);
			/*
			 * our category is not null...therefore we need to start filtering
			 * the searches by what has been selected by the user
			 */
			Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjectsForArchive(), category, value);// we
																											// are
																											// filtering
																											// through
																											// all
																											// the
																											// digital
																											// objects...look
																											// at
																											// filtering
																											// filtered
																											// objects
																											// when
																											// thinking
																											// about
																											// being
																											// able
																											// to
																											// filter
																											// the
																											// search
			request.setAttribute("browseCategory", category);
			request.setAttribute("categoryValue", value);
			request.getSession().setAttribute("objectsForArchive", Browse.getFilteredDigitalObjects());
		}
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
	}

}
