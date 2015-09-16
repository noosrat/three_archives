package common.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import common.fedora.FedoraException;
import exhibitions.ExhibitionController;
import maps.MapController;
import search.SearchController;

public class ServiceDelegator {
	// handle exceptions in this method

	private static final HashMap<String, Controller> controllers = new HashMap<String, Controller>();

	static {
		controllers.put("search", new SearchController());
		controllers.put("exhibitions", new ExhibitionController());
		controllers.put("general", new GeneralController());
		controllers.put("maps", new MapController());
	}

	public HashMap<String, Controller> getControllers() {
		return controllers;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String url = "index.jsp";
		try {
			if (request.getPathInfo().substring(1).contains("search") || request.getPathInfo().substring(1).contains("browse")) {

				url = controllers.get("search").execute(request, response);

			} else if (request.getPathInfo().substring(1).contains("exhibitions")) {
				url = controllers.get("exhibitions").execute(request, response);
			} else if (request.getPathInfo().substring(1).contains("snaps")) {
				url = controllers.get("general").execute(request, response);
			} else if (request.getPathInfo().substring(1).contains("sequins")) {
				url = controllers.get("general").execute(request, response);
			} else if (request.getPathInfo().substring(1).contains("harfield")) {
				url = controllers.get("general").execute(request, response);
			} else if (request.getPathInfo().substring(1).contains("maps")) {
				url = controllers.get("maps").execute(request, response);
			}
		} catch (Exception exception) {
			request.setAttribute("message", exception);
		}

		return url;
	}

}
