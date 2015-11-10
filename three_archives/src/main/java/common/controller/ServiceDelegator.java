package common.controller;

import history.HistoryController;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import maps.MapController;
import search.BrowseController;
import search.SearchController;
import uploads.UploadController;
import User.UserController;

import common.fedora.FedoraException;

import downloads.DownloadController;
import exhibitions.ExhibitionController;

/**
 * The {@code ServiceDelegator} class is reponsible for delegating the request
 * to the necessary controller dependent on what has been specified within the
 * URL. The controller is linked to a particular service and the service
 * delegator delegates processing and completion of the service by passing the
 * control to the controller. This class is the {@link ThreeArchiveServlet}'s
 * entry point into the application
 * 
 * @author mthnox003
 */
public class ServiceDelegator {

	/**
	 * The {@link HashMap} constant representing all the controllers available
	 * to be de delegated to
	 */
	private static final HashMap<String, Controller> CONTROLLERS = new HashMap<String, Controller>();

	/**
	 * Static initialiser initialising the controller values into the
	 * {@link #controllers} type
	 */
	static {
		CONTROLLERS.put("search", new SearchController());
		CONTROLLERS.put("exhibitions", new ExhibitionController());
		CONTROLLERS.put("general", new GeneralController());
		CONTROLLERS.put("maps", new MapController());
		CONTROLLERS.put("downloads", new DownloadController());
		CONTROLLERS.put("uploads", new UploadController());
		CONTROLLERS.put("browse", new BrowseController());
		CONTROLLERS.put("history", new HistoryController());
		CONTROLLERS.put("user", new UserController());
	}

	/**
	 * Gets the {@link HashMap} instance representing the controllers contained
	 * within the application
	 * 
	 * @return
	 */
	public HashMap<String, Controller> getControllers() {
		return CONTROLLERS;
	}

	/**
	 * The execute method is invoked from teh {@link ThreeArchivesServlet} and
	 * is where the service delegator passes control to the relevant service's
	 * controller based on the URL content. The execute method also ensures that
	 * the cookie information has been written once the relevant service has
	 * been executed
	 *
	 * @param request
	 *            {@link HttpServletRequest} instance representing the request
	 *            from the client
	 * @param response
	 *            {@link HttpServletResponse} instance representing the response
	 *            for the client
	 * @return {@link String} instance representing the jsp to dispatch to
	 * @throws FedoraException
	 * @throws Exception
	 */

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws FedoraException, Exception {
		String url = "index.jsp";
		/*
		 * System.out.println(request.getParameter("deletions")); if
		 * (request.getParameter("deletions")!=null &&
		 * !request.getParameter("deletions").equals("")) {
		 * System.out.println("in deletions"); String
		 * deletions=request.getParameter("deletions"); String delete[] =
		 * deletions.split(",");
		 * 
		 * for (int k=0; k<delete.length;k++){ download.removeFromCart(cart,
		 * delete[k]); }
		 */
		String pathInfo = request.getPathInfo().substring(1);
		try {
			if (pathInfo.contains("search")) {
				url = CONTROLLERS.get("search").execute(request, response);
			} else if (pathInfo.contains("browse")) {
				url = CONTROLLERS.get("browse").execute(request, response);
			} else if (pathInfo.contains("exhibition")) {
				url = CONTROLLERS.get("exhibitions").execute(request, response);
			} else if (pathInfo.contains("maps")) {
				url = CONTROLLERS.get("maps").execute(request, response);
			} else if (pathInfo.contains("downloads")) {
				url = CONTROLLERS.get("downloads").execute(request, response);
			} else if (pathInfo.contains("uploads")) {
				url = CONTROLLERS.get("uploads").execute(request, response);
			} else if (pathInfo.contains("history")) {
				url = CONTROLLERS.get("history").execute(request, response);
			} else if (pathInfo.contains("user")) {
				url = CONTROLLERS.get("user").execute(request, response);
			} else {
				System.out.println("PROCESSING GENERAL");
				url = CONTROLLERS.get("general").execute(request, response);
			}
		} catch (Exception exception) {
			request.setAttribute("message", "An error occured.  Please contact IT");
			System.out.println("An exception has occurred");
			exception.printStackTrace();

		}

		/**
		 * This is the request information to be persisted after each execution
		 * 
		 * 
		 * */
		((HistoryController) CONTROLLERS.get("history"))
				.persistNecessaryRequestInformation();
		return url;
	}

}
