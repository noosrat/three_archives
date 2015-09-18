package search;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.solr.client.solrj.SolrServerException;

import common.controller.Controller;
import common.fedora.FedoraDigitalObject;
import common.fedora.FedoraException;

public class SearchController implements Controller {

	private static Search search = new Search();

	public static Search getSearch() {
		return search;
	}

	public String execute(HttpServletRequest request, HttpServletResponse response) {
		String result = "WEB-INF/frontend/searchandbrowse/searchAndBrowse.jsp";
		request.setAttribute("searchCategories", retrieveSearchCategories());
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
		String requestPath = request.getPathInfo().substring(1);
		StringBuilder terms = new StringBuilder("");

		String limit = request.getParameter("limitSearch");
		if (limit != null && !limit.isEmpty() && limit.equalsIgnoreCase("limitSearch")) {
			System.out.println("LIMITING THE SEARCH TO THIS SPECIFIC COLLECTION");
			Set<FedoraDigitalObject> results = new HashSet<FedoraDigitalObject>();
			// we essentially just need to filter the result we had with the new
			// search term?
			// what we need to do here is construct a dolr search with all the
			// existing items and the new condition..
			Set<FedoraDigitalObject> exisitingObjects = (Set<FedoraDigitalObject>) request.getSession()
					.getAttribute("objects");
			System.out.println("Found " + exisitingObjects.size() + " exisitng objects");
			// we need to build the query for fedora
			terms.append("(");
			for (FedoraDigitalObject digitalObject : exisitingObjects) {
				terms.append("PID:\"").append(digitalObject.getPid()).append("\" OR ");
			}
			terms.delete(terms.length() - 2, terms.length()).append(")");
			// now we can add the rest of the actual query
			terms.append(" AND ");
			System.out.println("SOLR QUERY for limited search " + terms);
		}
		if (requestPath.contains("search_objects/category")) {
			// then do all the specific searching
			System.out.println("REQUEST PAHT IN search fedora digital objects by category " + requestPath);
			String[] splitPath = requestPath.split("=");
			String queryCategory = splitPath[0];
			String queryValue = splitPath[1];
			terms.append(queryCategory).append(":\"").append(queryValue).append("\"");
		} else {
			String s = request.getParameter("terms");
			if (s != null && !s.isEmpty()) {
				terms.append(s);
			} else {
				throw new FedoraException("Please enter search terms");

			}

		}

		System.out.println("Query constructed by category for solr is " + terms);
		Set<FedoraDigitalObject> digitalObjects = new HashSet<FedoraDigitalObject>();
		digitalObjects = getSearch().findFedoraDigitalObjects(new String(terms));
		System.out.println("Retrieved " + digitalObjects.size() + " objects in SOLR");
		if (!(digitalObjects == null || digitalObjects.isEmpty())) {
			// request.setAttribute("objects", (Object) digitalObjects);
			request.getSession().setAttribute("objects", digitalObjects);
		} else {
			request.setAttribute("message", (Object) "No results to return");
		}
	}

	public static TreeSet<String> retrieveSearchCategories() {
		TreeSet<String> result = new TreeSet<String>();
		for (SearchAndBrowseCategory cat : SearchAndBrowseCategory.values()) {
			result.add(cat.name());
		}
		return result;
	}

}
