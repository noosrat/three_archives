package common.controller;

import history.HistoryController;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import search.BrowseController;
import search.SearchController;

import common.model.ManageUsers;

import configuration.ArchivalService;
import configuration.PropertiesHandler;

public class GeneralController implements Controller {

	/**
	 * The {@link HashMap} instance housing properties for all the available
	 * archives
	 */
	private static HashMap<String, PropertiesHandler> ARCHIVES;

	/**
	 * This method is executed whenever the application navigates to the landing
	 * page and is to navigate into a new archive for exploration
	 * 
	 */
	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String result = "";
		ArrayList<String> cart = new ArrayList<String>();
		// cart.add("sss:1");
		// cart.add("sss:2");
		// cart.add("sq:sq5");
		// cart.add("sq:sq6");
		// cart.add("sq:sq7");
		// cart.add("sq:sq8");
		// cart.add("sq:sq9");
		// cart.add("sq:sq10");
		// cart.add("sq:sq11");
		ManageUsers userManager = new ManageUsers();
		// userManager.addUser(new User("admin","admin","ADMINISTRATOR"));
		// userManager.addUser(new User("student","student","privileged"));

		HttpSession session = request.getSession();
		clearArchiveSpecificSessionInformation(session);
		request.getSession().setAttribute("searchCategories",
				SearchController.retrieveSearchCategories());
		if (session.getAttribute("MEDIA_CART") == null) {
			session.setAttribute("MEDIA_CART", cart);
		}
		storeAllArchivePropertiesWithinSession(request);
		HistoryController historyController = new HistoryController();
		BrowseController browseController = new BrowseController();
		String pathInfo = request.getPathInfo().substring(1);
		System.out.println(pathInfo);

		/*
		 * the below retrieves all the digital objects from the digital
		 * repository
		 */
		if (session.getAttribute("objects") == null) {
			session.setAttribute("objects", SearchController.getSearch()
					.findFedoraDigitalObjects("*"));
			System.out.println(session.getAttribute("objects").toString());
		}
		/*
		 * calling this ensures that all browsing categories and objects have
		 * been initialised
		 */
		browseController.execute(request, response);
		/*
		 * this ensures that all the history functionality has been initialised
		 * for the specific archive being explored
		 */
		historyController.execute(request, response);

		/*
		 * the below ensures that the application dispatches to the relevant
		 * archive's home page
		 */
		String archive = (String) request.getSession().getAttribute(
				"ARCHIVE_CONCAT");
		String first = archive.substring(0, 1);
		result = first.toLowerCase() + archive.substring(1) + "Home.jsp";
		return result;

	}

	/**
	 * 
	 * This method is responsible for the loading of all the archives properties
	 * as well as storing these properties within the {@link #ARCHIVES} instance
	 * to allow for easy accessibility of the properties for all the archives.
	 * This aids the configurable manner of the archive and allows the archival
	 * services to be rendered enabled or disabled dependent on properties
	 * specified for the specific archive. The properties for a specific archive
	 * are then loaded into the session of the Request in order to ensure that
	 * the application reflects accordingly throughout the interaction with the
	 * chosen archive
	 * 
	 * @author mthnox003
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the request
	 *            from the client
	 */
	private void storeAllArchivePropertiesWithinSession(
			HttpServletRequest request) {
		if (ARCHIVES == null) {
			loadArchiveProperties();
		}
		System.out.println("PROCESSING ARCHIVE PROPERTIES");
		HttpSession session = request.getSession();
		if (request.getPathInfo() != null) {

			for (String archive : ARCHIVES.keySet()) {
				System.out.println("Properties file : " + archive);
				String name = ARCHIVES.get(archive).getProperty("archive.name")
						.replaceAll("[^a-zA-Z0-9\\s]", "")
						.replaceAll("\\s+", "");
				System.out.println("Archive name : " + name);
				if (request.getPathInfo().substring(1).equalsIgnoreCase(name)) {
					System.out.println("WE FOUND A MATCHING PROPERTIES FILE");
					session.setAttribute("ARCHIVE", ARCHIVES.get(archive)
							.getProperty("archive.name"));
					System.out.println("THIS IS THE ARCHIVE "
							+ session.getAttribute("ARCHIVE"));
					session.setAttribute("ARCHIVE_CONCAT", name);
					session.setAttribute("MEDIA_PREFIX", ARCHIVES.get(archive)
							.getProperty("archive.multimedia.prefix"));
					HashMap<String, String> services = new HashMap<String, String>();
					for (String property : ARCHIVES.get(archive)
							.getAllPropertyNames()) {
						System.out.println("PROCESSING ARCHIVE SERVICES");
						System.out.println("property : "
								+ property
								+ " value "
								+ Boolean.parseBoolean(ARCHIVES.get(archive)
										.getProperty(property)));
						if (property.contains("service.")
								&& Boolean.parseBoolean(ARCHIVES.get(archive)
										.getProperty(property))) {
							ArchivalService service = ArchivalService
									.parseServiceProperty(property.substring(8));
							services.put(service.getInterfaceName(),
									service.getRedirect_url());

						}
					}
					session.setAttribute("SERVICES", services);
					break;
				}
			}
		} else {
			request.setAttribute("message",
					"An error occured.  Please contact IT");
		}
	}

	/**
	 * 
	 * This is the information which is specific dependent on which archive is
	 * being browse. The information therefore needs to be cleared from the
	 * session since the user decided to explore another archive within the same
	 * browser session. This ensures that the correct cookies, tags and objects
	 * are reset allowing for re-assignment when accessing the next archive
	 * 
	 * @author mthnox003
	 * @param session
	 *            {@link HttpSession} instance representing the session of the
	 *            interaction
	 */
	private void clearArchiveSpecificSessionInformation(HttpSession session) {
		System.out.println("Clearing session information");
		session.setAttribute("browseCategoryCookie", null);
		session.setAttribute("objectsModifiedSinceLastVisit", null);
		session.setAttribute("categoriesWithRecentUpdates", null);
		session.setAttribute("userFavouriteCategoriesWithRecentUpdates", null);
		session.setAttribute("tagCloud", null);
		session.setAttribute("objectsForArchive", null);
		session.setAttribute("categoriesAndObjects", null);
		session.setAttribute("terms", null);
		System.out.println("Session information after clearing out");
	}

	/**
	 * 
	 * 
	 * This method iterates through all the .properties files contained within
	 * the configuration resources. These properties files each belong to a
	 * specific archive and their properties are then loaded and stored in the
	 * {@link #ARCHIVES} instance
	 *
	 * @author mthnox003
	 */
	private void loadArchiveProperties() {
		System.out.println("IN LOAD ARCHIVE PROPERTIES");
		ARCHIVES = new HashMap<String, PropertiesHandler>();
		ClassLoader classLoader = GeneralController.class.getClassLoader();
		File directory = new File(classLoader.getResource("configuration")
				.getFile());
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties")
						&& !file.getName().contains("general")) {
					ARCHIVES.put(file.getName(),
							new PropertiesHandler(file.getAbsolutePath()));
				}
			}
		}

	}

}
