package search;

import java.util.ArrayList;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import annotations.Annotations;
import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;
import downloads.Download;
import history.History;
import history.HistoryController;
import uploads.AutoCompleteUtility;

/**
 * The {@link BrowseController} is responsible for controlling the browsing
 * categories and objects on display
 * 
 * @author mthnox003
 *
 */
public class BrowseController implements Controller {

	/**
	 * The {@link Browse} instance used to perform browsing services
	 */
	private static Browse browse = new Browse();

	/**
	 * Gets the {@link #browse} instance representing the {@link Service} to
	 * conduct browsing through
	 * 
	 * @return {@link Browse} instance
	 */
	public Browse getBrowse() {
		return browse;
	}

	/**
	 * The execute method is present in each controller and allows for the
	 * execution of the request from the client
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the request
	 *            from the client
	 * @param response
	 *            {@link HttpServletResponse} instance representing the response
	 *            for the client
	 * @return {@link String} instance representing the next page to be
	 *         navigated to in the application. This returns the .jsp file path
	 *         to dispatch to
	 * @throws Exception
	 */
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";

		if (request.getParameter("annotations") != null
				&& !request.getParameter("annotations").isEmpty()) {
			performAnnotationsActions(request);
		}

		System.out.println(request.getParameter("addedtocart"));
		if (request.getParameter("addedtocart") != null
				&& !request.getParameter("addedtocart").isEmpty()) {
			performDownloadActions(request);
		}

		if (request.getPathInfo().contains("ORDER_BY")) {
			sortFedoraDigitalObjects(request);
		} else {
			getBrowse().setFedoraDigitalObjects(
					(Set<FedoraDigitalObject>) (request.getSession()
							.getAttribute("objects")));
			getBrowse().initialise(
					(String) request.getSession().getAttribute("MEDIA_PREFIX"));
			result = browseFedoraObjects(request, response);
			request.setAttribute("searchCategories",
					SearchController.retrieveSearchCategories());
			(new HistoryController()).execute(request, response);
			request.getSession().setAttribute("categoriesAndObjects",
					getBrowse().getCategorisedFedoraDigitalObjects());
		}
		request.getSession().setAttribute("searchTags", null);
		request.getSession().setAttribute("terms", null);

		return result;
	}

	/**
	 * @author noosrat
	 * @param request
	 * @throws FedoraException
	 * @throws Exception
	 */
	private void performAnnotationsActions(HttpServletRequest request)
			throws FedoraException, Exception {

		String pid = request.getParameter("pid");
		String annotations = request.getParameter("annotations");

		Set<FedoraDigitalObject> temp = (Set<FedoraDigitalObject>) request
				.getSession().getAttribute("objects");

		Annotations annotation = new Annotations();
		annotation.addAnnotation(pid, annotations, temp);
		SolrCommunicator.updateSolrIndex();
		request.getSession().setAttribute("objects",
				SearchController.getSearch().findFedoraDigitalObjects("*"));
		new AutoCompleteUtility().refreshAllAutocompleteFiles();

	}

	/**
	 * @author noosrat
	 * @param request
	 */
	private void performDownloadActions(HttpServletRequest request) {
		System.out.println("in add to car");
		ArrayList<String> cart = (ArrayList<String>) request.getSession()
				.getAttribute("MEDIA_CART");
		String selected = request.getParameter("addedtocart");
		System.out.println(selected);

		Download download = new Download();
		cart = download.addToCart(selected, cart);

		request.getSession().setAttribute("MEDIA_CART", cart);

	}

	/**
	 * This method sorts fedora digital object dependent on what field has been
	 * specified
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the request
	 */
	private void sortFedoraDigitalObjects(HttpServletRequest request) {
		String pathInfo = request.getPathInfo().substring(1);
		String[] values = pathInfo.split("=");
		Set<FedoraDigitalObject> objs = (Set<FedoraDigitalObject>) request
				.getSession().getAttribute("objectsForArchive");
		request.getSession().setAttribute("objectsForArchive",
				getBrowse().sortResults(values[1], objs));

	}

	/**
	 * Allows for the browsing of all fedora objects contained within the
	 * repository These objects are then filtered in order to ensure that the
	 * objects being browsed are those pertaining to the specific archive being
	 * explored
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instnace representing the client
	 *            request
	 * @throws FedoraException
	 * @throws SolrServerException
	 */
	private void browseAllFedoraObjects(HttpServletRequest request)
			throws FedoraException, SolrServerException {

		Set<FedoraDigitalObject> fedoraDigitalObjectsForArchive = getBrowse()
				.getFedoraDigitalObjectsForArchive();

		if (fedoraDigitalObjectsForArchive == null
				|| fedoraDigitalObjectsForArchive.isEmpty()) {
			request.setAttribute("message", "No results to retuRrn");
		}
		request.getSession().setAttribute("objectsForArchive",
				fedoraDigitalObjectsForArchive);
	}

	/**
	 * This allows for browsing of the digital objects within the specific
	 * archive dependent on a particular category
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the client
	 *            request
	 * @return {@link String} instance representing the .jsp to dispatch to
	 * @throws Exception
	 */
	private String browseFedoraObjects(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String category = request.getParameter("category");
		String value = request.getParameter(category);

		/*
		 * if the category is blank or null it means that it is just general
		 * browse
		 */
		if (category == null || category.isEmpty()) {
			browseAllFedoraObjects(request);
		} else {
			/*
			 * we want to add these values to our word cloud...both the category
			 * and the actual value
			 */
			History.addTextToTagCloud(category, false);
			request.setAttribute("browseCategory", category);

			if (value == null) {
				return "WEB-INF/frontend/searchandbrowse/browseCategory.jsp";
			}

			History.addTextToTagCloud(value, false);
			/*
			 * our category is not null...therefore we need to start filtering
			 * the searches by what has been selected by the user
			 */
			getBrowse().filterFedoraDigitalObjects(
					getBrowse().getFedoraDigitalObjectsForArchive(), category,
					value);
			request.setAttribute("categoryValue", value);

			request.getSession().setAttribute("objectsForArchive",
					getBrowse().getFilteredDigitalObjects());
		}
		request.setAttribute("browseCategories", getBrowse()
				.getBrowsingCategories());
		return "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
	}

}
