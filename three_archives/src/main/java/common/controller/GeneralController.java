package common.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.model.ManageUsers;
import configuration.ArchivalService;
import configuration.PropertiesHandler;
import history.HistoryController;
import search.BrowseController;
import search.SearchController;

public class GeneralController implements Controller {
	private static HashMap<String, PropertiesHandler> ARCHIVES;
	/*
	 * this is where we should actually set all the session variables that are
	 * specific to the archives.........
	 */
	public String execute(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String result = "";
		ArrayList<String> cart = new ArrayList<String>();
//		cart.add("sss:1");
//		cart.add("sss:2");
		//cart.add("sq:sq5");
		//cart.add("sq:sq6");
		//cart.add("sq:sq7");
		//cart.add("sq:sq8");
		//cart.add("sq:sq9");
		//cart.add("sq:sq10");
		//cart.add("sq:sq11");
		ManageUsers userManager= new ManageUsers();
	//userManager.addUser(new User("admin","admin","ADMINISTRATOR"));
//		userManager.addUser(new User("student","student","privileged"));

		HttpSession session = request.getSession();
		clearArchiveSpecificSessionInformation(session);
		request.getSession().setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		if(session.getAttribute("MEDIA_CART")==null){
			session.setAttribute("MEDIA_CART", cart);
		}
		storeAllArchivePropertiesWithinSession(request);
		HistoryController historyController = new HistoryController();
		BrowseController browseController = new BrowseController();
		String pathInfo =  request.getPathInfo().substring(1);
		System.out.println(pathInfo);

		if (session.getAttribute("objects") == null) {
			session.setAttribute("objects", SearchController.getSearch().findFedoraDigitalObjects("*"));//this is getting all of the archive documents...
			System.out.println(session.getAttribute("objects").toString());
		}
		browseController.execute(request, response);
		historyController.execute(request, response);
		String archive = (String) request.getSession().getAttribute("ARCHIVE_CONCAT");
		String first = archive.substring(0, 1);
		result = first.toLowerCase() + archive.substring(1) + "Home.jsp";
		return result;

	}

	private void storeAllArchivePropertiesWithinSession(HttpServletRequest request) {
		if (ARCHIVES==null){
			loadArchiveProperties();
		}
		System.out.println("PROCESSING ARCHIVE PROPERTIES");
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
					HashMap<String,String> services = new HashMap<String,String>();
					for (String property : ARCHIVES.get(archive).getAllPropertyNames()) {
						System.out.println("PROCESSING ARCHIVE SERVICES");
						System.out.println("property : " + property  + " value " + Boolean.parseBoolean(ARCHIVES.get(archive).getProperty(property)));
						if (property.contains("service.") && Boolean.parseBoolean(ARCHIVES.get(archive).getProperty(property))) {
							// we are dealing with services

							ArchivalService service =ArchivalService.parseServiceProperty(property.substring(8));
							services.put(service.getInterfaceName(),service.getRedirect_url());

						}
					}
					//instead of having many front-end service elements let us just send through a fully mapped services map 
					// Map<Service Frontend Name, Service Mapping>  for each of the archives dependent on what they have
					session.setAttribute("SERVICES", services);
					
					break;
				}
			}
		} else {
			request.setAttribute("message", "SOMETHING SEEMS TO HAVE GONE WRONG.  CONTACT IT");
		}
	}

//	private void testingAutoComplete() throws Exception {
//		HashMap<String, String> archives = new HashMap<String, String>();
//		// this needs to be thought out for the config layer.
//		archives.put("Sequins,Self and Struggle", "sq");
//		archives.put("Harfield Village", "hv");
//		archives.put("Movie Snaps", "ms");
//		AutoCompleteUtility.refreshAutocompleFile(archives);
//	}

	// the following all differ per archive
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

	private void loadArchiveProperties() {
		System.out.println("IN LOAD ARCHIVE PROPERTIES");
		ARCHIVES = new HashMap<String, PropertiesHandler>();
		ClassLoader classLoader = GeneralController.class.getClassLoader();
		File directory = new File(classLoader.getResource("configuration").getFile());
		if (directory != null) {
			for (File file : directory.listFiles()) {
				if (file.getName().contains(".properties") && !file.getName().contains("general")) {
					ARCHIVES.put(file.getName(), new PropertiesHandler(file.getAbsolutePath()));
				}
			}
		}

	}

}
