package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

import services.search.Search;


public class ServiceDelegator {
	// handle exceptions in this method
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String url = "index.jsp";
		try {
			if (request.getPathInfo().substring(1).equals("search")) {

				searchFedoraObjects(request, response);
			}
		} catch (FedoraClientException exception) {
			request.setAttribute("error", exception.getMessage());
		}

		return url;
	}

	private void searchFedoraObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraClientException {
		Search search = new Search();
		String terms = request.getParameter("terms");
		List<DatastreamProfile> pids = search.findObjects(terms);
		request.setAttribute("objects", pids);
	}

}
