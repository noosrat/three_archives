package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

import services.search.Search;

public class ServiceDelegator {
	// handle exceptions in this method
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String url = "search.jsp";
		try {
			if (request.getPathInfo().substring(1).equals("search")) {

				searchFedoraObjects(request, response);
			}
		} catch (FedoraClientException exception) {
			request.setAttribute("message", exception.getMessage());
		}

		return url;
	}

	enum Query {
		ALL("all"), ID("pid"), TITLE("title"), DESCRIPTION("description");

		private final String mapping;

		private Query(String mapping) {
			this.mapping = mapping;
		}
	}

	private void searchFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraClientException {
		Search search = new Search();

		String terms = request.getParameter("terms");
		String query = request.getParameter("query");

		List<DatastreamProfile> pids = new ArrayList<DatastreamProfile>();

		if (query != null && !query.equalsIgnoreCase(Query.ALL.mapping)) {
			pids = search.findObjectsWithQuery(query + "~*" + terms + "*");

		} else {
			pids = search.findObjects(terms);
		}

		if (pids != null && !pids.isEmpty()) {
			request.setAttribute("objects", pids);
		} else {
			request.setAttribute("message", "No results to return");
		}
	}

}
