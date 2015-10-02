package history;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;

public class HistoryController implements Controller {

	private static History history = new History();
	private static String archive;
	private static HashMap<String, String> defaultCookies = new HashMap<String, String>();

	static {
		defaultCookies.put("dateLastVisited", new Date().toString());
		defaultCookies.put("HarfieldVillageCategories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");
		defaultCookies.put("SequinsSelfandStruggleCategories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");
		defaultCookies.put("MovieSnapsCategories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0");
	}

	public static History getHistory() {
		return history;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// the following two lines need to happen as soon as we select which
		// archive we are looking in...before selecting browse
		String pathInfo = request.getPathInfo().substring(1);
		if (pathInfo.equalsIgnoreCase("SequinsSelfandStruggle") || pathInfo.equalsIgnoreCase("MovieSnaps")
				|| pathInfo.equalsIgnoreCase("HarfieldVillage")) {
			archive = ((String) request.getSession().getAttribute("ARCHIVE")).replaceAll("[^a-zA-Z0-9\\s]", "")
					.replaceAll("\\s+", "");
			initialiseUserCookies(request, response);
			return null;
		} else {
			initialiseUserSessionAttributesFromCookies(request, response);

			if (request.getPathInfo().contains("browse")) {
				updateCategoryCookieBasedOnCategoryBrowse(request, response);
			}
			displayUserSessioAtt(request);
		}
		// for every request we make we must append to the wordle json sting
		// within the request....and then here we persist it
		return "WEB-INF/frontend/historyandstatistics/history.jsp";
	}

	private void displayUserSessioAtt(HttpServletRequest req) {
		HttpSession session = req.getSession();
		System.out.println(
				"***********************************************************************************************************************");
		System.out.println("dateLastVisited: " + session.getAttribute("dateLastVisited"));
		System.out.println("browseCategoryCookie" + ((Cookie) session.getAttribute("browseCategoryCookie")).getValue());
		// System.out.println("objectsModifiendSinceLastVisit "
		// + ((ArrayList<FedoraDigitalObject>)
		// session.getAttribute("objectsModifiedSinceLastVisit")).toString());
		System.out.println("categoriesRecentUpdates "
				+ ((HashMap<String, TreeSet<String>>) session.getAttribute("categoriesWithRecentUpdates")).keySet()
						.toString());
		System.out.println("userFavourites"
				+ ((ArrayList<String>) session.getAttribute("userFavouriteCategoriesWithRecentUpdates")).toString());
		System.out.println(
				"***********************************************************************************************************************");

	}

	private void initialiseUserCookies(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Initialising user cookies ********************************");
		Cookie[] cookies = request.getCookies();
		ArrayList<String> cookieNames = new ArrayList<String>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieNames.add(cookie.getName());
				System.out.println("User cookie found ....name: " + cookie.getName() + " value: " + cookie.getValue());
			}
		}
		for (String defaultCookie : defaultCookies.keySet()) {
			if (!cookieNames.contains(defaultCookie)) {
				System.out.println("Cookie not found for: " + defaultCookie);
				// the cookie is not there so we must add it
				response.addCookie(initialiseNewCookie(defaultCookie, defaultCookies.get(defaultCookie)));

			}
		}
		// now since there are cookies in the request and response...let us add
		// them to the session to be used throughout?
	}

	private void initialiseUserSessionAttributesFromCookies(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		HttpSession session = request.getSession();
		// initiliase this using the cookies
		if (session.getAttribute("dateLastVisited") == null) {
			System.out.println("No value for dateLastVisited within the session");
			extractAndProcessDateLastModified(request, response);
		}
		if (session.getAttribute("browseCategoryCookie") == null) {
			System.out.println("No value for browseCategoryCookie");
			session.setAttribute("browseCategoryCookie", getBrowseCategoryCookie(request, response));
			System.out.println("New value for browseCategoryCookie "
					+ ((Cookie) session.getAttribute("browseCategoryCookie")).getValue());
		}
		if (session.getAttribute("objectsModifiedSinceLastVisit") == null) {
			System.out.println("No value for objectsModifiedSinceLastVisit within the session");
			ArrayList<FedoraDigitalObject> objs = getHistory()
					.retrieveRecentlyUpdateItems((String) session.getAttribute("dateLastVisited"));
			session.setAttribute("objectsModifiedSinceLastVisit", objs);
		}
		if (session.getAttribute("categoriesWithRecentUpdates") == null) {
			System.out.println("No value for updated objects within the session");
			ArrayList<FedoraDigitalObject> digitalObjects = (ArrayList<FedoraDigitalObject>) request.getSession()
					.getAttribute("objectsModifiedSinceLastVisit");
			HashMap<String, TreeSet<String>> updates = getHistory().categoriesRecentlyUpdated(digitalObjects);
			System.out.println("SIZE OF THE UPDATED CATEGORIES " + updates.size());
			session.setAttribute("categoriesWithRecentUpdates", updates);

		}
		if (session.getAttribute("userFavouriteCategiresWithRecentUpdates") == null) {
			System.out.println("No value for user favouritecategories within the session");
			HashMap<String, TreeSet<String>> categories = (HashMap<String, TreeSet<String>>) request.getSession()
					.getAttribute("categoriesWithRecentUpdates");
			ArrayList<String> favouriteCategories = getHistory().favouriteBrowsingCategoryUpdates(categories,
					(Cookie) session.getAttribute("browseCategoryCookie"));
			session.setAttribute("userFavouriteCategoriesWithRecentUpdates", favouriteCategories);

		}
		if (session.getAttribute("tagCloud") == null) {
			System.out.println("The tagcloud value has not been set for the session");
			String filename = "../webapps/data/" + archive + "TagCloud.txt";
			session.setAttribute("tagCloud", getHistory().constructTagCloud(filename));
		}
		// if(session.) this last one is for what is generally browsed but this
		// will be stored in a file anyway
		// now we can continually modifiy the cookies and not worry about them
		// being affected for this current session...
	}

	private Cookie initialiseNewCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(365 * 24 * 60 * 60 * 1000);
		cookie.setPath("/");
		return cookie;
	}

	private void extractAndProcessDateLastModified(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Cookie[] cookies = request.getCookies();
		String date = null;
		for (Cookie cookie : cookies) {
			if (cookie.getName().equals("dateLastVisited")) {
				date = cookie.getValue();
				// cookie.setValue(null);
				// cookie.setPath("/");
				// cookie.setMaxAge(0); don't do this yet otherwise will have to
				// response.addCookie(cookie);
				// change data
				System.out.println("Found date last visited cookie " + date);
				break;
			}
		}
		if (date == null) {
			System.out.println("did not find date last visited cookie ");
			date = new Date().toString();
		}
		// regardless of whether it is their first visit or not, we need to add
		// a new cookie of current date to the response..but also make sure
		// we have added our entry date cookie into the session
		// response.addCookie(initialiseNewCookie("dateLastVisited", new
		// Date().toString()));
		request.getSession().setAttribute("dateLastVisited", date);
	}

	private Cookie getBrowseCategoryCookie(HttpServletRequest request, HttpServletResponse response) {
		System.out.println("Getting browse category cookie for " + archive);
		Cookie[] cookies = request.getCookies();
		System.out.println("COOKIES: " + cookies.length);
		for (Cookie cookie : cookies) {
			if (cookie.getName().equalsIgnoreCase(archive + "Categories")) {
				return cookie;
			}
		}
		return null;

	}

	/* this must happen with every browse initiated */
	private void updateCategoryCookieBasedOnCategoryBrowse(HttpServletRequest request, HttpServletResponse response) {
		Cookie cookie = getBrowseCategoryCookie(request, response);
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
				categories.replace(indexBegin, indexEnd, fields[0] + ":" + change);
				System.out.println("New categories " + categories);
			}

		}
		// now iterate through the existing cookies to make sure this one is
		// removed before we add it to the response
		Cookie[] cookies = request.getCookies();
		for (Cookie c : cookies) {
			if (c.getName().equalsIgnoreCase(cookie.getName())) {
				c.setValue(null);
				c.setPath("/");
				c.setMaxAge(0);
				response.addCookie(c);
			}
		}
		cookie.setValue(categories.toString());
		System.out.println("OUR NEW CATEGORY COOKIE JUST ABOUT TO ADD TO THE RESPONSE ..Name=" + cookie.getName()
				+ " Value= " + cookie.getValue());
		response.addCookie(initialiseNewCookie(cookie.getName(), cookie.getValue()));

	}

	public static void persistNecessaryRequestInformation() {
		String filename = "../webapps/data/" + archive + "TagCloud.txt";
		getHistory().persistTagCloudText(filename);
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder("Collection:4##Event:3");
		String splitCookie = "Collection:4";
		int indexBegin = sb.indexOf(splitCookie);
		int indexEnd = indexBegin + splitCookie.length();
		sb.replace(indexBegin, indexEnd, "Collection" + ":" + 200000002);
		System.out.println("New categories " + sb);

		String a = "Sequins,Self and Struggle";

		String b = a.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");

		System.out.println(b);
	}

}
