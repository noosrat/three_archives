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
import User.UserController;

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
		controllers.put("user", new UserController());
	}

	public HashMap<String, Controller> getControllers() {
		return controllers;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response){
		String url = "index.jsp";
		System.out.println(request.getParameter("addedtocart"));
		if(request.getParameter("addedtocart")!=null){
			System.out.println("in add to car");
			ArrayList<String> cart = (ArrayList<String>) request.getSession().getAttribute("MEDIA_CART");
			String selected=request.getParameter("addedtocart");
			String[] selectedList=selected.split(">");
			
			for (int k=0; k<selectedList.length; k++){
				cart.add(selectedList[k]);
			
			request.getSession().setAttribute("MEDIA_CART", cart);	
			}
			
			
		}
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
			} 
			else if (pathInfo.contains("user")) {
				url = controllers.get("user").execute(request, response);
			}else {
				System.out.println("PROCESSING GENERAL");
				url = controllers.get("general").execute(request, response);
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
	

}