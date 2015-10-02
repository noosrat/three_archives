package common.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import downloads.DownloadController;
import common.fedora.FedoraDigitalObject;
import exhibitions.ExhibitionController;
import history.HistoryController;
import maps.MapController;
import search.BrowseController;
import search.SearchController;
import uploads.UploadController;

public class ServiceDelegator {
	// handle exceptions in this method

	private static final HashMap<String, Controller> controllers = new HashMap<String, Controller>();

	static {
		controllers.put("search", new SearchController());
		controllers.put("exhibitions", new ExhibitionController());
		controllers.put("general", new GeneralController());
		controllers.put("maps", new MapController());
		controllers.put("downloads", new DownloadController());
		controllers.put("uploads", new UploadController());
		controllers.put("browse", new BrowseController());
		controllers.put("history", new HistoryController());
	}

	public HashMap<String, Controller> getControllers() {
		return controllers;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response){
		String url = "index.jsp";
		String pathInfo = request.getPathInfo().substring(1);
		try {
			if (pathInfo.contains("search")) {
				url = controllers.get("search").execute(request, response);
			} else if (pathInfo.contains("browse")) {
				url = controllers.get("browse").execute(request, response);
			} else if (pathInfo.contains("exhibition")) {
				url = controllers.get("exhibitions").execute(request, response);
			}else if (pathInfo.contains("maps")) {
				url = controllers.get("maps").execute(request, response);
			}else if (pathInfo.contains("downloads")) {
				url = controllers.get("downloads").execute(request, response);
			} else if (pathInfo.contains("uploads")) {
				url = controllers.get("uploads").execute(request, response);
			} else if (pathInfo.contains("history")) {
				url = controllers.get("history").execute(request, response);
			} else {
				System.out.println("PROCESSING GENERAL");
				url = controllers.get("general").execute(request, response);
				clearSessionInformation(request.getSession());//this is done when they go back to the homepage of personal histories...so that cookies don't overlap into the differnet archives
			}
		} catch (Exception exception) {
			request.setAttribute("message", exception.getMessage());
			System.out.println(exception);

			} 
		persistAllRequestInformation();
		return url;
	}

	/* this is everything to be persisted after each request is executed */
	private void persistAllRequestInformation() {
		HistoryController.persistNecessaryRequestInformation();

	}
	//the following all differ per archive
	private void clearSessionInformation(HttpSession session){
		System.out.println("Clearing session information");
		session.setAttribute("browseCategoryCookie", null);
		session.setAttribute("objectsModifiedSinceLastVisit", null);
		session.setAttribute("categoriesWithRecentUpdates", null);
		session.setAttribute("userFavouriteCategoriesWithRecentUpdates", null);
		session.setAttribute("tagCloud", null);
		System.out.println("Session information after clearing out");
		
		System.out.println("Category cookie: " + session.getAttribute("browseCategoryCookie") + " TagCloud " + session.getAttribute("tagCloud"));
		
		
		
		

	}

}