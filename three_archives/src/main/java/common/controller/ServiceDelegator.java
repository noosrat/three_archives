package common.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import exhibitions.ExhibitionController;
import maps.MapController;
import search.BrowseController;
import search.SearchController;

public class ServiceDelegator {
	// handle exceptions in this method

	private static final HashMap<String, Controller> controllers = new HashMap<String, Controller>();

	static {
		controllers.put("search", new SearchController());
		controllers.put("exhibitions", new ExhibitionController());
		controllers.put("maps", new MapController());
		controllers.put("browse", new BrowseController());
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
			} else if (pathInfo.contains("maps")) {
				url = controllers.get("maps").execute(request, response);
			}
		} catch (Exception exception) {
			request.setAttribute("message", exception);
			exception.printStackTrace();
		}

		return url;
	}

}