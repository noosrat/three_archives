package history;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;

/**
 * The {@code HistoryController} is the {@link Controller} responsible for
 * managing the user cookies and ensuring the correct information is returned to
 * the user about recent updates to the digital objects and collections
 * 
 * @author mthnox003
 *
 */
public class HistoryController implements Controller {

	/**
	 * The {@link History} instance to communicate with the service
	 */
	private History history = new History();
	/**
	 * The {@link String} instance representing the archive being investigated
	 */
	private static String archive;

	/**
	 * The defaultCookies for any user
	 */
	// private static HashMap<String, String> defaultCookies = new
	// HashMap<String, String>();
	//
	// static {
	// defaultCookies.put("dateLastVisited", new Date().toString());
	// defaultCookies
	// .put("HarfieldVillageCategories",
	// "Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");
	// defaultCookies
	// .put("SequinsSelfandStruggleCategories",
	// "Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");
	// defaultCookies
	// .put("MovieSnapsCategories",
	// "Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");
	// }
	//
	/**
	 * Gets the {@link Service} responsible for managing the user's history
	 * 
	 * @return {@link History} instance to be used to manage the user's history
	 */
	public History getHistory() {
		return history;
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
		String pathInfo = request.getPathInfo().substring(1);
		archive = (String) request.getSession().getAttribute("ARCHIVE_CONCAT");
		System.out.println("PATH INFO iNSIDE HISTORY" + pathInfo);
		if (pathInfo.contains(archive)) {
			initialiseUserCookies(request, response);
			return null;
		} else {
			initialiseUserSessionAttributesFromCookies(request, response);

			if (request.getPathInfo().contains("browse")) {
				updateCategoryCookieBasedOnCategoryBrowse(request, response);
			}
		}
		if (pathInfo.contains("history")) {
			filterFedoraDigitalObjects(request);
		}
		return "WEB-INF/frontend/historyandstatistics/history.jsp";
	}

	/**
	 * This method filters the fedora digital objects being displayed on the
	 * front-end dependent on the filter category and filter value specified
	 * 
	 * @param request
	 */
	private void filterFedoraDigitalObjects(HttpServletRequest request) {
		String category = request.getParameter("category");
		String value = request.getParameter(category);

		System.out.println("FILTERING UPDATED FEDORA OBJECTS WITH " + category
				+ " " + value);
		if (category == null) {
			return;
		}
		if (value == null && value.isEmpty()) {
			request.setAttribute("message",
					"An error occured.  Please contact IT");
			return;
		}
		Set<FedoraDigitalObject> objectsToFilter = (Set<FedoraDigitalObject>) request
				.getSession().getAttribute("objectsModifiedSinceLastVisit");
		Set<FedoraDigitalObject> filteredObjects = getHistory()
				.filterFedoraDigitalObjects(objectsToFilter, category, value);
		request.setAttribute("objectsModifiedSinceLastVisit", filteredObjects);

	}

	/**
	 * This method is responsible for the initialisation of the user cookies.
	 * This method reads in the user cookies from the request and checks for the
	 * existence of the default cookies, if any of the default cookies are not
	 * present they are initialised and added
	 * 
	 * @param request
	 * @param response
	 */
	private void initialiseUserCookies(HttpServletRequest request,
			HttpServletResponse response) {
		System.out
				.println("Initialising user cookies ********************************");
		Cookie[] cookies = request.getCookies();
		ArrayList<String> cookieNames = new ArrayList<String>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieNames.add(cookie.getName());
				System.out.println("User cookie found ....name: "
						+ cookie.getName() + " value: " + cookie.getValue());
			}
		}

		if (!cookieNames.contains("dateLastVisited")) {
			System.out.println("Cookie not found for: " + "dateLastVisited");
			response.addCookie(initialiseNewCookie("dateLastVisited",
					new Date().toString()));

		}

		// for (String defaultCookie : defaultCookies.keySet()) {
		// if (!cookieNames.contains(defaultCookie)) {
		// System.out.println("Cookie not found for: " + defaultCookie);
		// response.addCookie(initialiseNewCookie(defaultCookie,
		// defaultCookies.get(defaultCookie)));
		//
		// }
		// }
	}

	/**
	 * This method allows for the initialisation of the user session attributes
	 * pertaining to the user's cookies
	 * 
	 * @param request
	 *            {@link HttpServletRequest} to be used to obtain cookie
	 *            information
	 * @param response
	 *            {@link HttpServletResponse} instance to be used to re-set
	 *            cookies
	 * @throws Exception
	 */
	private void initialiseUserSessionAttributesFromCookies(
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// initiliase this using the cookies
		if (session.getAttribute("dateLastVisited") == null) {
			System.out
					.println("No value for dateLastVisited within the session");
			request.getSession().setAttribute("dateLastVisited",
					extractAndProcessDateLastVisited(request, response));
		}
		if (session.getAttribute("browseCategoryCookie") == null) {
			System.out.println("No value for browseCategoryCookie");
			session.setAttribute("browseCategoryCookie",
					getBrowseCategoryCookie(request));
			System.out.println("New value for browseCategoryCookie "
					+ ((Cookie) session.getAttribute("browseCategoryCookie"))
							.getValue());
		}
		if (session.getAttribute("objectsModifiedSinceLastVisit") == null) {
			System.out
					.println("No value for objectsModifiedSinceLastVisit within the session");
			getHistory().retrieveRecentlyUpdateItems(
					(String) session.getAttribute("dateLastVisited"));
			Set<FedoraDigitalObject> objs = getHistory()
					.getObjectsSinceLastVisit();
			session.setAttribute("objectsModifiedSinceLastVisit", objs);
		}
		if (session.getAttribute("categoriesWithRecentUpdates") == null) {
			System.out
					.println("No value for updated objects within the session");
			HashSet<FedoraDigitalObject> digitalObjects = (HashSet<FedoraDigitalObject>) request
					.getSession().getAttribute("objectsModifiedSinceLastVisit");
			HashMap<String, TreeSet<String>> updates = getHistory()
					.categoriesRecentlyUpdated(digitalObjects);
			System.out.println("SIZE OF THE UPDATED CATEGORIES "
					+ updates.size());
			session.setAttribute("categoriesWithRecentUpdates", updates);

		}
		if (session.getAttribute("userFavouriteCategoriesWithRecentUpdates") == null) {
			System.out
					.println("No value for user favouritecategories within the session");
			HashMap<String, TreeSet<String>> categories = (HashMap<String, TreeSet<String>>) request
					.getSession().getAttribute("categoriesWithRecentUpdates");
			ArrayList<String> favouriteCategories = getHistory()
					.favouriteBrowsingCategoryUpdates(
							categories,
							(Cookie) session
									.getAttribute("browseCategoryCookie"));
			session.setAttribute("userFavouriteCategoriesWithRecentUpdates",
					favouriteCategories);

		}
		if (session.getAttribute("tagCloud") == null) {
			System.out
					.println("The tagcloud value has not been set for the session");
			String filename = "../webapps/data/" + archive + "TagCloud.txt";
			session.setAttribute("tagCloud",
					getHistory().constructTagCloud(filename));
		}

	}

	/**
	 * The method that initialises a new cookie by creating it, setting a
	 * maximum age and setting the path for the cookie
	 * 
	 * @param name
	 *            {@link String} value representing the cookie's name
	 * @param value
	 *            {@link String} instance representing the value of the actual
	 *            cookie
	 * @return {@link Cookie} instance representing the initiliased cookie
	 */
	private Cookie initialiseNewCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(365 * 24 * 60 * 60 * 1000);
		cookie.setPath("/");
		return cookie;
	}

	/**
	 * This method extracts the cookie of the user's date last visited
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the client
	 *            request
	 * @param response
	 *            {@link HttpServletResponse} instance representing the response
	 * @return {@link String} instance representing the date
	 * @throws Exception
	 */
	private String extractAndProcessDateLastVisited(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Cookie[] cookies = request.getCookies();
		String date = null;

		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("dateLastVisited")) {
					date = cookie.getValue();
					cookie.setValue(null);
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				System.out.println("Found date last visited cookie " + date);
					break;
				}
			}
		}
		if (date == null) {
			System.out.println("did not find date last visited cookie ");
			date = new Date().toString();
		}
		// regardless of whether it is their first visit or not, we need to add
		// a new cookie of current date to the response..but also make sure
		// we have added our entry date cookie into the session // the one for
		// testing is Mon Sep 28 14:49:16 SAST 2015
		// response.addCookie(initialiseNewCookie("dateLastVisited",
		// "Mon Sep 28 14:49:16 SAST 2015"));
//		response.addCookie(initialiseNewCookie("dateLastVisited",
//				new Date().toString()));
		//this was done since fedora was registering a time two hours delayed
		Calendar c =Calendar.getInstance();
		c.add(Calendar.HOUR,-2);

		response.addCookie(initialiseNewCookie("dateLastVisited",
				c.getTime().toString()));
		return date;
	}
	
	/**
	 * Gets the browse category cookie indicating how often the user browses
	 * each of the specified categories
	 * 
	 * @param request
	 *            {@link HttpServletRequest} instance representing the client
	 *            request
	 * @return {@link Cookie} instance containing the browser cookie for the
	 *         specific archive
	 */
	private Cookie getBrowseCategoryCookie(HttpServletRequest request) {
		System.out.println("Getting browse category cookie for " + archive);
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			System.out.println("COOKIES: " + cookies.length);
			for (Cookie cookie : cookies) {
				if (cookie.getName() != null) {
					if (cookie.getName().equalsIgnoreCase(
							archive + "Categories")) {
						return cookie;
					}
				}
			}
		}

		return initialiseNewCookie(
				request.getSession().getAttribute("ARCHIVE_CONCAT")
						+ "Categories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");

	}

	/* this must happen with every browse initiated */
	/**
	 * This occurs with every browse initiated (any browse category accessed)
	 * This method updates the browseCategoryCookie for the specific archive for
	 * the specific user with data obtained from the user's interaction with the
	 * interface
	 * 
	 * @param request
	 *            {@link HttpServletRequest} holding the browser cookie to be
	 *            changed
	 * @param response
	 *            {@link HttpServletResponse} instance that the cookie gets
	 *            updated with
	 */
	private void updateCategoryCookieBasedOnCategoryBrowse(
			HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = getBrowseCategoryCookie(request);
		StringBuilder categories = new StringBuilder();
		categories = new StringBuilder(cookie.getValue());
		System.out.println("Old categories " + categories);
		String categoryToIncrement = request.getParameter("category");

		String[] splitCookies = new String(categories).split("##");
		System.out.println("Category cookie and values " + splitCookies);
		for (String splitCookie : splitCookies) {
			String[] fields = splitCookie.split(":");
			if (fields[0].equalsIgnoreCase(categoryToIncrement)) {
				int change = Integer.parseInt(fields[1]) + 1;
				int indexBegin = categories.indexOf(splitCookie);
				int indexEnd = indexBegin + splitCookie.length();
				categories.replace(indexBegin, indexEnd, fields[0] + ":"
						+ change);
				System.out.println("New categories " + categories);
			}

		}
		// now iterate through the existing cookies to make sure this one is
		// removed before we add it to the response
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie c : cookies) {
				if (c.getName().equalsIgnoreCase(cookie.getName())) {
					c.setValue(null);
					c.setPath("/");
					c.setMaxAge(0);
					response.addCookie(c);
				}
			}
		}
		cookie.setValue(categories.toString());
		System.out
				.println("OUR NEW CATEGORY COOKIE JUST ABOUT TO ADD TO THE RESPONSE ..Name="
						+ cookie.getName() + " Value= " + cookie.getValue());
		response.addCookie(initialiseNewCookie(cookie.getName(),
				cookie.getValue()));

	}

	/**
	 * Thi method persists the tagcloud text at then end of the user's
	 * interaction with the specific archive
	 */
	public void persistNecessaryRequestInformation() {
		String filename = "../webapps/data/" + archive + "TagCloud.txt";
		getHistory().persistTagCloudText(filename);
	}
}
