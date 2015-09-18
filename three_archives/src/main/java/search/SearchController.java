package search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import common.controller.Controller;
import common.fedora.Datastream;
import common.fedora.DatastreamID;
import common.fedora.DublinCoreDatastream;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class SearchController implements Controller {

	private static Search search = new Search();

	public static Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";

		String requestPath = request.getPathInfo().substring(1);
		if (requestPath != null) {
			try {
				if (requestPath.contains("search_objects")) {
					searchFedoraDigitalObjects(request, response);
				}
			} catch (SolrServerException exception) {
				request.setAttribute("message",
						"Something seems to have gone wrong with SOLR" + exception.getStackTrace());
			} catch (FedoraException exception) {
				request.setAttribute("message",
						"Something seems to have gone wrong with Fedora" + exception.getStackTrace());

			}
		}
		return result;

	}

	private void searchFedoraDigitalObjects(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException, SolrServerException {

		String terms;
		// try {
		// terms = URLEncoder.encode(request.getParameter("terms"), "UTF-8"); is
		// this really necessary since solr will do it?
		terms = request.getParameter("terms");
		// } catch (UnsupportedEncodingException e) {
		// throw new FedoraException("Could not find fedora object due to faulty
		// search", e);
		// }

		Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
		if (terms == null || terms.trim().isEmpty()) {
			throw new FedoraException("Please enter search terms");
		}
		digitalObjects = getSearch().findFedoraDigitalObjects(terms);

		if (digitalObjects != null && !digitalObjects.isEmpty()) {
			request.setAttribute("objects", digitalObjects);
		} else {
			request.setAttribute("message", "No results to return");
		}

	}



	private void searchFedoraDatastreams(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException {

		String terms;
		try {
			terms = URLEncoder.encode(request.getParameter("terms"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FedoraException("Could not find fedora object due to faulty search", e);
		}

		HashMap<String,Datastream> pids = new HashMap<String,Datastream>();
		if (terms == null || terms.trim().isEmpty()) {
			throw new FedoraException("Please enter search terms");
		}
		pids = getSearch().findFedoraDatastreams(terms);

		if (pids != null && !pids.isEmpty()) {
			request.setAttribute("objects", pids);
		} else {
			request.setAttribute("message", "No results to return");
		}
		

	}

}
