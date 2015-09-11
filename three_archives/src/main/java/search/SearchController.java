package search;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.Controller;
import common.fedora.Datastream;
import common.fedora.FedoraException;

public class SearchController implements Controller {

	private static Search search = new Search();

	public Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request,
			HttpServletResponse response) {
		String result = "WEB-INF/frontend/searchandbrowse/search_home.jsp";

		if (request.getPathInfo().substring(1).contains("redirect_search")) {
			result = "WEB-INF/frontend/searchandbrowse/search_home.jsp";
		} else if (request.getPathInfo().substring(1).contains("search_objects")) {
			result = searchFedoraObjects(request, response);
		}
		return result;
	}

	private String searchFedoraObjects(HttpServletRequest request,
			HttpServletResponse response) {

		String terms = request.getParameter("terms");
		String query = request.getParameter("query");

		List<Datastream> pids = new ArrayList<Datastream>();
		try {
			pids = getSearch().findObjects(terms);
		} catch (Exception ex) {
			request.setAttribute("message", ex.getStackTrace());
		}

		if (pids != null && !pids.isEmpty()) {
			request.setAttribute("objects", pids);
		} else {
			request.setAttribute("message", "No results to return");
		}
		// }

		// List<DatastreamProfile> pids = new ArrayList<DatastreamProfile>();
		//
		// if (terms == null || terms.trim().isEmpty()) {
		// request.setAttribute("message", "Please enter a search term");
		// } else {
		// if (query != null && !query.equalsIgnoreCase(Query.ALL.mapping)) {
		// pids = getSearch().findObjectsWithQuery(
		// query + "~*" + terms + "*");
		// } else {
		// pids = getSearch().findObjects(terms);
		// }
		//
		// if (pids != null && !pids.isEmpty()) {
		// request.setAttribute("objects", pids);
		// } else {
		// request.setAttribute("message", "No results to return");
		// }
		// }
		return "WEB-INF/frontend/searchandbrowse/search_home.jsp";
	}

	enum Query {
		ALL("all"), ID("pid"), TITLE("title"), DESCRIPTION("description");

		private final String mapping;

		private Query(String mapping) {
			this.mapping = mapping;
		}
	}
}
