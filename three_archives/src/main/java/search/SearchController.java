package search;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

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

	public Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String result = "WEB-INF/frontend/searchandbrowse/search_home.jsp";

		try {
			if (request.getPathInfo().substring(1).contains("browse")) {
				result = browseFedoraObjects(request,response);
			} else if (request.getPathInfo().substring(1).contains("search_objects")) {
				result = searchFedoraDigitalObjects(request, response);
			}
			if (request.getPathInfo().substring(1).contains("redirect_search")) {
				result = "WEB-INF/frontend/searchandbrowse/search_home.jsp";
			} else if (request.getPathInfo().substring(1).contains("search_objects")) {
				result = searchFedoraDigitalObjects(request, response);
			}
		} catch (SolrServerException exception) {
			request.setAttribute("message", "Something seems to have gone wrong with SOLR" + exception.getStackTrace());
		} catch (FedoraException exception) {
			request.setAttribute("message",
					"Something seems to have gone wrong with Fedora" + exception.getStackTrace());

		}
		return result;

	}
	
	private String browseFedoraObjects(HttpServletRequest request, HttpServletResponse response) throws FedoraException, SolrServerException{
	 //this is to return everything in the archive collection...this is just to illustrate browse temporarily

		List<FedoraDigitalObject> digitalObjects = new ArrayList<FedoraDigitalObject>();
	
		digitalObjects = getSearch().findFedoraDigitalObjects("*");

		if (digitalObjects != null && !digitalObjects.isEmpty()) {
			request.setAttribute("objects", digitalObjects);
			request.setAttribute("dublinCoreDatastreams", populateDublinCoreDatastream(digitalObjects));
		} else {
			request.setAttribute("message", "No results to return");
		}

		return "WEB-INF/frontend/searchandbrowse/search_home.jsp";
	}

	private String searchFedoraDigitalObjects(HttpServletRequest request, HttpServletResponse response)
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

		List<FedoraDigitalObject> digitalObjects = new ArrayList<FedoraDigitalObject>();
		if (terms == null || terms.trim().isEmpty()) {
			throw new FedoraException("Please enter search terms");
		}
		digitalObjects = getSearch().findFedoraDigitalObjects(terms);

		if (digitalObjects != null && !digitalObjects.isEmpty()) {
			request.setAttribute("objects", digitalObjects);
			request.setAttribute("dublinCoreDatastreams", populateDublinCoreDatastream(digitalObjects));
		} else {
			request.setAttribute("message", "No results to return");
		}

		return "WEB-INF/frontend/searchandbrowse/search_home.jsp";
	}

	/*
	 * had to do this since JSTL doesn't allow type casting
	 */
	private ArrayList<DublinCoreDatastream> populateDublinCoreDatastream(
			List<FedoraDigitalObject> fedoraDigitalObjects) {
		ArrayList<DublinCoreDatastream> dublinCoreDatastreams = new ArrayList<DublinCoreDatastream>();
		for (FedoraDigitalObject digitalObject : fedoraDigitalObjects) {
			int index = digitalObject.getDatastreams().indexOf(new DublinCoreDatastream(digitalObject.getPid()));
				dublinCoreDatastreams.add((DublinCoreDatastream) (digitalObject.getDatastreams().get(index)));
		}

		return dublinCoreDatastreams;

	}

	private String searchFedoraDatastreams(HttpServletRequest request, HttpServletResponse response)
			throws FedoraException {

		String terms;
		try {
			terms = URLEncoder.encode(request.getParameter("terms"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new FedoraException("Could not find fedora object due to faulty search", e);
		}

		List<Datastream> pids = new ArrayList<Datastream>();
		if (terms == null || terms.trim().isEmpty()) {
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
