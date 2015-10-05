package common.controller;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import history.HistoryController;
import search.Browse;
import search.BrowseController;
import search.SearchController;
import uploads.AutoCompleteUtility;

public class GeneralController implements Controller {

	/*
	 * this is where we should actually set all the session variables that are
	 * specific to the archives.........
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ArrayList<String> cart = new ArrayList<String>();
		cart.add("ms:1");

		HttpSession session = request.getSession();
		clearArchiveSessionInformation(session);
		session.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		HistoryController historyController = new HistoryController();
		BrowseController browseController = new BrowseController();
		if (session.getAttribute("objects") == null) {
			session.setAttribute("objects", SearchController.getSearch().findFedoraDigitalObjects("*"));//this is getting all of the archive documents...
		}

		String pathInfo = request.getPathInfo().substring(1);

//		testingAutoComplete();
		
		if (pathInfo.equalsIgnoreCase("SequinsSelfAndStruggle")) {
			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
			session.setAttribute("mediaPrefix", "sq");
			session.setAttribute("MEDIA_CART", cart);
			browseController.execute(request, response);
			historyController.execute(request, response);
			return "sequinsSelfAndStruggleHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("MovieSnaps")) {
			session.setAttribute("ARCHIVE", "Movie Snaps");
			session.setAttribute("mediaPrefix", "ms");
			session.setAttribute("MEDIA_CART", cart);
			browseController.execute(request, response);
			historyController.execute(request, response);
			return "movieSnapsHome.jsp";

		} else if (pathInfo.equalsIgnoreCase("HarfieldVillage")) {
			session.setAttribute("ARCHIVE", "Harfield Village");
			session.setAttribute("mediaPrefix", "hv");
			session.setAttribute("MEDIA_CART", cart);
			browseController.execute(request, response);
			historyController.execute(request, response);
			return "harfieldVillageHome.jsp";

		}

		return "index.jsp";
	}

	private void testingAutoComplete() throws Exception {
		HashMap<String, String> archives = new HashMap<String, String>();
		// this needs to be thought out for the config layer.
		archives.put("Sequins,Self and Struggle", "sq");
		archives.put("Harfield Village", "hv");
		archives.put("Movie Snaps", "ms");
		AutoCompleteUtility.refreshAutocompleFile(archives);
	}

	// the following all differ per archive
	private void clearArchiveSessionInformation(HttpSession session) {
		System.out.println("Clearing session information");
		session.setAttribute("browseCategoryCookie", null);
		session.setAttribute("objectsModifiedSinceLastVisit", null);
		session.setAttribute("categoriesWithRecentUpdates", null);
		session.setAttribute("userFavouriteCategoriesWithRecentUpdates", null);
		session.setAttribute("tagCloud", null);
		session.setAttribute("objectsForArchive", null);
		session.setAttribute("mediaPrefix", null);
		session.setAttribute("categoriesAndObjects", null);
		System.out.println("Session information after clearing out");

		System.out.println("Category cookie: " + session.getAttribute("browseCategoryCookie") + " TagCloud "
				+ session.getAttribute("tagCloud"));

	}

}