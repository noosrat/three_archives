package history;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;

public class HistoryController implements Controller {

	private static History history = new History();
	private static HashMap<String, String> defaultCookies = new HashMap<String, String>();

	static {
		defaultCookies.put("dateLastVisited", new Date().toString());
		defaultCookies.put("HarfieldVillageCategories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0##");
		defaultCookies.put("SequinsSelfandStruggleCategories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0##");
		defaultCookies.put("MovieSnapsCategories",
				"Collection:0##Creator:0##Event:0##Exhibition:0##Location:0##Subject:0##Contributor:0##Source:0##");
	}

	public static History getHistory() {
		return history;
	}

	//this will normally get called right at the beginning of the interaction with the search service?....but also when people are browsing around
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// we will first use the existing date to extract the updated
		// information before we actually begin manipulating it and then reset
		// it to today..maybe this should rather be reet when we invalidate the
		// session?
		initialiseUserCookies(request, response);
		extractAndProcessDateLastModified(request, response);
		//we need to call our category browse option here but only if the url has browse in it
		if (request.getPathInfo().contains("browse")){
			updateCategoryCookieBasedOnCategoryBrowse(request, response);
		}
		refreshCookieExpirationDate(request);
		return "WEB-INF/frontend/historyandstatistics/history.jsp";
	}

	private static void initialiseUserCookies(HttpServletRequest request, HttpServletResponse response) {
		// get all teh cookies...if they have a value, that's fine..if don't
		// exist then initialise and add
		System.out.println("Initialising user cookies ********************************");
		Cookie[] cookies = request.getCookies();
		ArrayList<String> cookieNames = new ArrayList<String>();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				cookieNames.add(cookie.getName());
			}
		}
		System.out.println("Cookies found " + cookieNames.toString());
		for (String defaultCookie : defaultCookies.keySet()) {
			if (!cookieNames.contains(defaultCookie)) {
				System.out.println("Cookie not found for: " + defaultCookie);
				// the cookie is not there so we must add it
				response.addCookie(initialiseNewCookie(defaultCookie, defaultCookies.get(defaultCookie)));
			}
		}
	}

	private static Cookie initialiseNewCookie(String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(365 * 24 * 60 * 60 * 1000);
		cookie.setPath("/");
		return cookie;
	}

	private void extractAndProcessDateLastModified(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		// first check if the session in general already has this attribute. If
		// it does then use that one..if it doesn't it means we need to obtain
		// it from
		// the cookie and set it into the session and then reset the one inthe
		// cookie to now...every time

		Cookie[] cookies = request.getCookies();
		if (request.getSession().getAttribute("dateLastVisited") == null) {
			System.out.println("No dateLastVisited within the session");
			// if it is null we must extract it from the cooke and set it...
			// otherwise if is not null then we just skip over the entire
			// resetting process
			String date = null;
			if (cookies != null) {
				for (int i = 0; i < cookies.length; i++) {
					if (cookies[i].getName().equals("dateLastVisited")) {
						date = cookies[i].getValue();
						System.out.println("Found date last visited cookie " + date);
					}
				}
			}
			if (date == null) {
				// this is the first time they are visiting the site..we must
				// assign
				// them a date cookie...
				response.addCookie(initialiseNewCookie("dateLastVisited", new Date().toString()));
				System.out.println("did not find date last visited cookie ");
				// otherwise we use the date that is currently there but we also
				// reset the date to today...maybe do this everytime in the
				// execute
			}
			request.getSession().setAttribute("dateLastVisited", date);
		}

		// now we can process/use the date to retrieve information etc but we
		// can reset the one that is in the actual cookie
		ArrayList<FedoraDigitalObject> objs = retrieveRecentlyUpdateItems(
				(String) request.getSession().getAttribute("dateLastVisited"));
		request.setAttribute("objectsModifiedSinceLastVisit", objs);
		HashMap<String, TreeSet<String>> updates = getHistory().itemsUpdated(objs);
		System.out.println("SIZE OF THE UPDATED CATEGORIES " + updates.size());
		request.setAttribute("categoriesWithRecentUpdates", updates);
		System.out.println("THESE ARE THE ITEMS THAT HAVE BEEN UPDATED/ADDED SINCE YOUR LAST VISIT");
		for (FedoraDigitalObject o : objs) {
			System.out.println(o.getPid() + " " + o.getDateLastModified());

		}
	}

	private static ArrayList<FedoraDigitalObject> retrieveRecentlyUpdateItems(String date) throws Exception {
		// we need to parse the date to be date format
		// dow mon dd hh:mm:ss zzz yyyy

		SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy");
		Date dateLastVisited = new Date();// it the date does not get parsed we
											// will just use today's date
		try {
			dateLastVisited = formatter.parse(date);
		} catch (ParseException e) {
			System.out.println(e);
			throw new Exception("Unable to parse date ", e);
		}
		System.out.println("PARSED DATE " + dateLastVisited);

		// now that we have parsed the date we can get the updated objects
		ArrayList<FedoraDigitalObject> fedoraDigitalObjects = getHistory()
				.retrieveDigitalObjectsAlteredSinceLastVisit(dateLastVisited);
		return fedoraDigitalObjects;

	}

	/*this would be done and updated during the browsing process...with each browse call..this should get processed*/
	
	private static void updateCategoryCookieBasedOnCategoryBrowse(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		// get the cookie of the specific collection and the specific category
		String archive = ((String) request.getSession().getAttribute("ARCHIVE")).replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+","")+"Categories";
		
		StringBuilder categories = new StringBuilder();

		/* these are the cookies we want to have */

		// get the respective cookie
		Cookie[] cookies = request.getCookies();
		// our cookies won't be null since we will have at least initiliased the
		// user cookies
		for (int index = 0; index < cookies.length; index++) {
			if (cookies[index].getName().equalsIgnoreCase(archive)) {
				categories = new StringBuilder(cookies[index].getValue());
				System.out.println("Old categories " + categories);
				String categoryToIncrement =  request.getParameter("category"); // this will come from the
													// request or from input in
													// generall...
				// now we can start looking at the individual categories
				String[] splitCookies = new String(categories).split("##");
				System.out.println("Category cookie and values " + splitCookies);
				for (String splitCookie : splitCookies) {
					String[] fields = splitCookie.split(":");
					// then should we just increment the stuff here and then
					// re-insert
					// it into the cookie....going to have to update the cookie
					// with
					// every request to make sure that we do not lose any
					// ifnormation
					if (fields[0].equalsIgnoreCase(categoryToIncrement)) {
						int change = Integer.parseInt(fields[1]) + 1;
						// now we need to find the value in the cookie and
						// update it
						int indexBegin = categories.indexOf(splitCookie);
						int indexEnd = indexBegin + splitCookie.length();
						categories.replace(indexBegin, indexEnd, fields[0] + ":" + change);
						System.out.println("New categories " + categories);
					}

				}
				// now that we have incremented the relevant category we can
				// update the cookie
				cookies[index].setValue(categories.toString());
				response.addCookie(cookies[index]);
				break; // break out of the loop since we have found the relevant
						// cookie, the relevant category and incremented it
			}
		}

	}
	
	private static void retrieveTopThreeBrowsingCategories(){
		
	}
	
	private static void changesInTopThreeCategories(){
		
	}

	/* not sure when exactly to do the below */
	private static void refreshCookieExpirationDate(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		System.out.println("COOOOOOOKIES " + cookies.length);
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				cookies[i].setMaxAge(365 * 24 * 60 * 60 * 1000);
				if (cookies[i].getName().equals("dateLastVisited")) {
					cookies[i].setValue(new Date().toString());// maybe do this
																// anyway...keep
																// date last
																// modified we
																// are working
																// with within
																// int the
																// session and
																// use that
																// one..when
																// session is
																// over the
																// value won't
																// be there and
																// therefore we
																// will have to
																// set it again
				}
			}
		}
	}

	public static void main(String[] args) {
		StringBuilder sb = new StringBuilder("Collection:4##Event:3");
		String splitCookie = "Collection:4";
		int indexBegin = sb.indexOf(splitCookie);
		int indexEnd = indexBegin + splitCookie.length();
		sb.replace(indexBegin, indexEnd, "Collection" + ":" + 200000002);
		System.out.println("New categories " + sb);
		
		String a = "Sequins,Self and Struggle";
		
		String b = a.replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+","");
		
		System.out.println(b);
	}

}
