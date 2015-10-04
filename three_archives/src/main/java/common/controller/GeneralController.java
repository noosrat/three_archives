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
import search.SearchController;

public class GeneralController implements Controller {
	private static HashMap<String, PropertiesHandler> ARCHIVES;
	//here we need a static map associating each service with a url ending
	private final static HashMap<String,String> services = new HashMap<String, String>();
	
	static{
		services.put("searchandbrowse", "browse");
		services.put("historyandstatistics", "");
		services.put("exhibitions","redirect_exhibitions" );
		services.put("uploads", "redirect_uploads");
		services.put("maps", "redirect_maps");
		services.put("downloads", "redirect_downloads");
		services.put("annotations", "");
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String result = "";
		
		ArrayList<String> cart = new ArrayList<String>();
		cart.add("ms:1");
		HttpSession session = request.getSession();
		request.setAttribute("searchCategories", SearchController.retrieveSearchCategories());
		session.setAttribute("MEDIA_CART", cart);
		storeAllArchivePropertiesWithinSession(request);
		HistoryController historyController = new HistoryController();
		historyController.execute(request, response);

		return "home.jsp";
		
	}
	private void storeAllArchivePropertiesWithinSession(HttpServletRequest request){
		loadArchiveProperties();
		HttpSession session = request.getSession();
		if (request.getPathInfo()!=null){
		for (String archive: ARCHIVES.keySet()){
			System.out.println("Properties file : " + archive);
			String name = ARCHIVES.get(archive).getProperty("archive.name").replaceAll("[^a-zA-Z0-9\\s]", "").replaceAll("\\s+", "");
			System.out.println("Archive name : " + name);
			if (request.getPathInfo().substring(1).equalsIgnoreCase(name)){
				session.setAttribute("ARCHIVE", ARCHIVES.get(archive).getProperty("archive.name"));
				System.out.println("THIS IS THE ARCHIVE " + session.getAttribute("ARCHIVE"));
				session.setAttribute("ARCHIVE_CONCAT", name);
				session.setAttribute("MEDIA_PREFIX", ARCHIVES.get(archive).getProperty("archive.multimedia.prefix"));
				HashMap<String,Boolean> services = new HashMap<String,Boolean>();
				for (String property: ARCHIVES.get(archive).getAllPropertyNames()){
					if (property.contains("service.")){
						//we are dealing with services
						services.put(property.substring(8),Boolean.parseBoolean(ARCHIVES.get(archive).getProperty(property)));
					}
				}
			}
				break;
			}
		}else{
			request.setAttribute("message", "SOMETHING SEEMS TO HAVE GONE WRONG.  CONTACT IT");
		}
		
	}
		
//		
//		if (pathInfo.equalsIgnoreCase("SequinsSelfAndStruggle")) {
//			session.setAttribute("ARCHIVE", "Sequins, Self and Struggle");
//			session.setAttribute("MEDIA_CART", cart);
//			historyController.execute(request, response);
//			return "sequinsSelfAndStruggleHome.jsp";
//
//		} else if (pathInfo.equalsIgnoreCase("MovieSnaps")) {
//			session.setAttribute("ARCHIVE", "Movie Snaps");
//			session.setAttribute("MEDIA_CART", cart);
//			historyController.execute(request, response);
//			return "movieSnapsHome.jsp";
//
//		} else if (pathInfo.equalsIgnoreCase("HarfieldVillage")) {
//			session.setAttribute("ARCHIVE", "Harfield Village");
//			session.setAttribute("MEDIA_CART", cart);
//			historyController.execute(request, response);
//			return "harfieldVillageHome.jsp";
//
//		}
//
//		return "index.jsp";
	


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