package common.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import configuration.IndexPage;
import configuration.PropertiesHandler;
import history.HistoryController;
import search.BrowseController;
import search.SearchController;
import uploads.AutoCompleteUtility;

public class GeneralController implements Controller {
	private static HashMap<String, PropertiesHandler> ARCHIVES;
	// here we need a static map associating each service with a url ending
	private final static HashMap<String, String> services = new HashMap<String, String>();

	static {
		services.put("searchandbrowse", "browse");
		services.put("historyandstatistics", "");
		services.put("exhibitions", "redirect_exhibitions");
		services.put("uploads", "redirect_uploads");
		services.put("maps", "redirect_maps");
		services.put("downloads", "redirect_downloads");
		services.put("annotations", "");
	}

	/*
	 * this is where we should actually set all the session variables that are
	 * specific to the archives.........
	 */
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";

		ArrayList<String> cart = new ArrayList<String>();
		cart.add("ms:1");
		HttpSession session = request.getSession();
		clearArchiveSessionInformation(session);
		request.getSession().setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		session.setAttribute("MEDIA_CART", cart);
		storeAllArchivePropertiesWithinSession(request);
		HistoryController historyController = new HistoryController();
		BrowseController browseController = new BrowseController();

		if (session.getAttribute("objects") == null) {
			session.setAttribute("objects", SearchController.getSearch().findFedoraDigitalObjects("*"));//this is getting all of the archive documents...
		}
		
		browseController.execute(request, response);
		historyController.execute(request, response);
		String archive = (String) request.getSession().getAttribute("ARCHIVE_CONCAT");
		String first = archive.substring(0, 1);
		result = first.toLowerCase() + archive.substring(1) + "Home.jsp";
		return result;
		// "home.jsp";

	}

	private void storeAllArchivePropertiesWithinSession(HttpServletRequest request) {
		loadArchiveProperties();
		HttpSession session = request.getSession();
		if (request.getPathInfo() != null) {
			for (String archive : ARCHIVES.keySet()) {
				System.out.println("Properties file : " + archive);
				String name = ARCHIVES.get(archive).getProperty("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "")
						.replaceAll("\\s+", "");
				System.out.println("Archive name : " + name);
				if (request.getPathInfo().substring(1).equalsIgnoreCase(name)) {
					System.out.println("WE FOUND A MATCHING PROPERTIES FILE");
					session.setAttribute("ARCHIVE", ARCHIVES.get(archive).getProperty("archive.name"));
					System.out.println("THIS IS THE ARCHIVE " + session.getAttribute("ARCHIVE"));
					session.setAttribute("ARCHIVE_CONCAT", name);
					session.setAttribute("MEDIA_PREFIX",
							ARCHIVES.get(archive).getProperty("archive.multimedia.prefix"));
					HashMap<String, Boolean> services = new HashMap<String, Boolean>();
					for (String property : ARCHIVES.get(archive).getAllPropertyNames()) {
						if (property.contains("service.")) {
							// we are dealing with services
							services.put(property.substring(8),
									Boolean.parseBoolean(ARCHIVES.get(archive).getProperty(property)));
						}
					}
					break;
				}
			}
		} else {
			request.setAttribute("message", "SOMETHING SEEMS TO HAVE GONE WRONG.  CONTACT IT");
		}
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
		session.setAttribute("categoriesAndObjects", null);
		System.out.println("Session information after clearing out");
	}

	private void loadArchiveProperties() {
		ARCHIVES = new HashMap<String, PropertiesHandler>();
		ClassLoader classLoader = IndexPage.class.getClassLoader();
		File directory = new File(classLoader.getResource("configuration").getFile());
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties") && !file.getName().contains("general")) {
					ARCHIVES.put(file.getName(), new PropertiesHandler("configuration/" + file.getName()));
				}
			}
		}

	}

}