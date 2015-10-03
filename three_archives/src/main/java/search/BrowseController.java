

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
		Browse.setFedoraDigitalObjects((Set<FedoraDigitalObject>)(request.getSession().getAttribute("objects")));
		Browse.initialise((String) request.getSession().getAttribute("mediaPrefix"));
		// String result = browseFedoraObjects(request, response);
		browseFedoraObjects(request, response);
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		(new HistoryController()).execute(request, response);
		// return result;
		request.setAttribute("categoriesAndObjects", Browse.getCategorisedFedoraDigitalObjects());
		return "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
	}

	private void browseAllFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException, SolrServerException {

		// this is to return everything in the archive collection...this is just
		// to illustrate browse temporarily
		/* we are intiially searching all the fedora objects here */
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

		if (fedoraDigitalObjectsForArchive == null || fedoraDigitalObjectsForArchive.isEmpty()) {
			request.setAttribute("message", "No results to retuRrn");
		}
		request.getSession().setAttribute("objectsForArchive", fedoraDigitalObjectsForArchive);
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
			Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjectsForArchive(), category, value);
			request.setAttribute("browseCategory", category);
			request.setAttribute("categoryValue", value);
			request.getSession().setAttribute("objectsForArchive", Browse.getFilteredDigitalObjects());
		}
		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
	}

}
//package search;
//
//import java.util.Set;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.solr.client.solrj.SolrServerException;
//
//import common.controller.Controller;
//import common.fedora.FedoraDigitalObject;
//import common.fedora.FedoraException;
//import history.History;
//import history.HistoryController;
//
//public class BrowseController implements Controller {
//
//	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		Browse.setFedoraDigitalObjects((Set<FedoraDigitalObject>) (request.getSession().getAttribute("objects")));
//		Browse.initialise((String) request.getSession().getAttribute("mediaPrefix"));
//		// String result = browseFedoraObjects(request, response);
//		browseFedoraObjects(request, response);
//		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
//		(new HistoryController()).execute(request, response);
//		// return result;
//		return "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
//		// return "WEB-INF/frontend/searchandbrowse/browseCategory.jsp";
//	}
//
//	private void browseAllFedoraObjects(HttpServletRequest request, HttpServletResponse response)
//			throws FedoraException, SolrServerException {
//
//		// this is to return everything in the archive collection...this is just
//		// to illustrate browse temporarily
//		/* we are intiially searching all the fedora objects here */
//		Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive = Browse.getFedoraDigitalObjectsForArchive(); // we
//																												// cannot
//																												// do
//																												// this
//																												// without
//																												// first
//																												// getting
//																												// all
//																												// objects
//		// before we actually set the attribute we need to filter per archive
//
//		if (fedoraDigitalObjectsForArchive == null || fedoraDigitalObjectsForArchive.isEmpty()) {
//			request.setAttribute("message", "No results to retuRrn");
//		}
//		request.getSession().setAttribute("objectsForArchive", fedoraDigitalObjectsForArchive);
//	}
//
//	private String browseFedoraObjects(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		String category = request.getParameter("category");
//		String value = request.getParameter(category);
//		String result = null;
//		/*
//		 * if the category is blank or null it means that it is just general
//		 * browse
//		 */
//		if (category == null || category.isEmpty()) {
//			browseAllFedoraObjects(request, response);
//			result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
//		} else {
//			History.addTextToTagCloud(category);
//
//			if ((value == null || value.isEmpty())) {
//				// we must check if value is null....if it is null then we must
//				// just navigate to the other jsp with the chosen category
//				// details
//				// so value is null which means ew have mearly clicked on a
//				// category link on the side menu..we haven't filtered furtgere
//				// the value is null but we still want to show the actual
//				// objects that lie within that category at least...
//				//we want all the objects still but they must be categoriesed into the folders --all those objects which fall into any of those folders
//				
//				result = "WEB-INF/frontend/searchandbrowse/browseCategory.jsp";
//			} else {
//
//				// none of them are null...everything has a value
//
//				/*
//				 * we want to add these values to our word cloud...both the
//				 * category and the actual value
//				 * 
//				 */
//
////				History.addTextToTagCloud(category);
//				History.addTextToTagCloud(value);
//				/*
//				 * our category is not null...therefore we need to start
//				 * filtering the searches by what has been selected by the user
//				 */
//				//we can perform the below once we have actually clicked on a folder
//				Browse.filterFedoraDigitalObjects(Browse.getFedoraDigitalObjectsForArchive(), category, value);
//				request.setAttribute("categoryValue", value);
//				request.setAttribute("browseCategory", category);
//				request.getSession().setAttribute("objectsForArchive", Browse.getFilteredDigitalObjects());
//			}
//		}
//		request.setAttribute("browseCategories", Browse.getBrowsingCategories());
//		return result;
//	}
//
//}
