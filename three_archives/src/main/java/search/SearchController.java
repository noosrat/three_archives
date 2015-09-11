package search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.Controller;
import common.fedora.Datastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class SearchController implements Controller {

	private static Search search = new Search();

	public Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request,
			HttpServletResponse response) throws FedoraException {
		String result = "WEB-INF/frontend/searchandbrowse/search_home.jsp";

		if (request.getPathInfo().substring(1).contains("redirect_search")) {
			result = "WEB-INF/frontend/searchandbrowse/search_home.jsp";
		} else if (request.getPathInfo().substring(1)
				.contains("search_objects")) {
			result = searchFedoraDigitalObjects(request, response);
		}
		return result;
	}

	
	private String searchFedoraDigitalObjects(HttpServletRequest request,
			HttpServletResponse response) throws FedoraException {

		String terms;
		try {
			terms = URLEncoder.encode(request.getParameter("terms"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FedoraException("Could not find fedora object due to faulty search", e);
		}

		List<FedoraDigitalObject> digitalObjects = new ArrayList<FedoraDigitalObject>();
		if (terms==null || terms.trim().isEmpty()){
			throw new FedoraException("Please enter search terms");
		}
		digitalObjects = getSearch().findFedoraDigitalObjects(terms);

		if (digitalObjects != null && !digitalObjects.isEmpty()) {
			request.setAttribute("objects", digitalObjects);
		} else {
			request.setAttribute("message", "No results to return");
		}

		return "WEB-INF/frontend/searchandbrowse/search_home.jsp";
	}
	private String searchFedoraDatastreams(HttpServletRequest request,
			HttpServletResponse response) throws FedoraException {

		String terms;
		try {
			terms = URLEncoder.encode(request.getParameter("terms"),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FedoraException("Could not find fedora object due to faulty search", e);
		}

		List<Datastream> pids = new ArrayList<Datastream>();
		if (terms==null || terms.trim().isEmpty()){
			throw new FedoraException("Please enter search terms");
		}
		pids = getSearch().findFedoraDatastreams(terms);

		if (pids != null && !pids.isEmpty()) {
			request.setAttribute("objects", pids);
		} else {
			request.setAttribute("message", "No results to return");
		}

		return "WEB-INF/frontend/searchandbrowse/search_home.jsp";
	}

}
